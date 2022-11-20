package com.drozd.ecaps.service;

import com.drozd.ecaps.configuration.SpaceGoogleAuthorizationCodeDto;
import com.drozd.ecaps.exception.BadArgumentException;
import com.drozd.ecaps.security.google.identity.GoogleIdentityVerificationService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import static com.drozd.ecaps.configuration.GoogleServicesConfiguration.APPLICATION_NAME;
import static com.drozd.ecaps.configuration.GoogleServicesConfiguration.JSON_FACTORY;

@Service
@RequiredArgsConstructor
public class GoogleApiService {
    public static final String DRIVE_FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";
    public static final List<String> DRIVE_FILE_SCOPES = List.of("email", "profile", "https://www.googleapis.com/auth/drive.file",
            "openid", "https://www.googleapis.com/auth/userinfo.profile", "https://www.googleapis.com/auth/userinfo.email");

    private final FileDataStoreFactory fileDataStoreFactory;
    private final GoogleClientSecrets clientSecrets;
    private final NetHttpTransport httpTransport;
    private final GoogleIdentityVerificationService verificationService;

    private final Logger LOG = LoggerFactory.getLogger(GoogleApiService.class);


    public String authorizeGoogleApiAuthorizationCodeAndGetDriveAccountEmail(final SpaceGoogleAuthorizationCodeDto authorizationCode, final String requestOrigin)
            throws IOException, BadArgumentException, GeneralSecurityException {

        if (!authorizationCode.scopes().contains(DriveScopes.DRIVE_FILE)) {
            throw new BadArgumentException("You have to select the Google Drive scope to authorize access to google drive for space.");
        }

        final List<String> scopes = Arrays.asList(authorizationCode.scopes().split(" +"));

        GoogleAuthorizationCodeFlow flow = getOfflineAuthorizationFlowScopesDriveFile(scopes);

        final GoogleTokenResponse tokenResponse = flow.newTokenRequest(authorizationCode.code())
                .setRedirectUri(requestOrigin)
                .execute();

        final String driveAccountEmail =
                verificationService.verify(tokenResponse.getIdToken())
                        .getPayload()
                        .getEmail();

        flow.createAndStoreCredential(tokenResponse, driveAccountEmail);

        return driveAccountEmail;

    }

    public File createFolderOrGetIfAlreadyExists(Credential credential, String folderName) throws IOException {
        Drive drive = getDriveService(credential);

        var foldersWithName = drive.files().list()
                .setQ("name = '" + folderName + "' and mimeType = 'application/vnd.google-apps.folder'")
                .execute()
                .getFiles();
        if (foldersWithName.isEmpty()) {
            File folderMetadata = new File()
                    .setMimeType(DRIVE_FOLDER_MIME_TYPE)
                    .setName(folderName);

            return drive.files()
                    .create(folderMetadata)
                    .execute();

        } else return foldersWithName.get(0);

    }

    private GoogleAuthorizationCodeFlow getOfflineAuthorizationFlowScopesDriveFile(List<String> scopes) throws IOException {
        return new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, scopes)
                .setDataStoreFactory(fileDataStoreFactory)
                .setAccessType("offline")
                .build();
    }

    @Nullable
    public Credential getCredential(String spaceDriveAccountEmail) throws IOException {
        return getOfflineAuthorizationFlowScopesDriveFile(DRIVE_FILE_SCOPES)
                .loadCredential(spaceDriveAccountEmail);
    }

    public List<String> getFiles(String userId) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, /*put Credential here*/ null)
                .setApplicationName(APPLICATION_NAME)
                .build();
        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();

        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            return List.of("No files found.");
        } else {
            return files.stream().map(File::getName).toList();
        }
    }

    public File uploadFileToFolder(String folderName, String fileName, String contentType, InputStream fileContent, Credential credential) throws IOException {
        Drive service = getDriveService(credential);

        var foldersWithName = service.files().list()
                .setQ("name = '" + folderName + "' and mimeType = 'application/vnd.google-apps.folder'")
                .execute()
                .getFiles();

        File fileMetadata = new File()
                .setName(fileName);

        if (!foldersWithName.isEmpty())
            fileMetadata.setParents(List.of(foldersWithName.get(0).getId()));

        var mediaContent = new InputStreamContent(contentType, fileContent);

        return service.files().create(fileMetadata, mediaContent).execute();
    }

    public File uploadFile(String fileName, String contentType, InputStream fileContent, Credential credential) throws IOException {
        Drive service = getDriveService(credential);

        File fileMetadata = new File()
                .setName(fileName);

        var mediaContent = new InputStreamContent(contentType, fileContent);

        return service.files().create(fileMetadata, mediaContent).execute();
    }

    private Drive getDriveService(Credential credential) {
        return new Drive.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public File downloadFileToOutputStreamAndGetMetadata(String fileId, Credential credential, OutputStream outputStreamForDownloadedFile) throws IOException {
        Drive drive = getDriveService(credential);
        var file = drive.files().get(fileId).execute();
        drive.files().get(fileId).executeMediaAndDownloadTo(outputStreamForDownloadedFile);
        return file;
    }

}
