package ru.mirea.malyushin.mireaproj.ui.filework;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import ru.mirea.malyushin.mireaproj.databinding.FragmentFileWorkBinding;

public class FileWorkFragment extends Fragment {

    private FileWorkViewModel mViewModel;
    private FragmentFileWorkBinding binding;
    private static final int REQUEST_CODE_FILE_EXPLORER = 1;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int REQUEST_CODE_FILE_CHOOSER = 101;
    private Uri selectedFileUri;

    public static FileWorkFragment newInstance() {
        return new FileWorkFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FileWorkViewModel.class);
        binding = FragmentFileWorkBinding.inflate(inflater, container, false);

        binding.fileExplorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileExplorer();
            }
        });

        return binding.getRoot();
    }

    private void openFileExplorer() {
//        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
//                    REQUEST_CODE_PERMISSION);
//        } else {
        int storagePermissionStatus = ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int storagePermissionStatus1 = ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (storagePermissionStatus1 != PackageManager.PERMISSION_GRANTED || storagePermissionStatus
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_CODE_PERMISSION);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/plain");
            startActivityForResult(intent, REQUEST_CODE_FILE_CHOOSER);
            }
    }

    private void convertTxtToDocx() {
        if (selectedFileUri != null) {
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedFileUri);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                XWPFDocument document = new XWPFDocument();
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    run.setText(line);
                    run.addBreak();
                }

                String outputFilePath = Environment.getExternalStorageDirectory() + "/output.docx";
                FileOutputStream fos = new FileOutputStream(outputFilePath);
                document.write(fos);

                fos.close();
                document.close();

                Toast.makeText(getActivity(), "Conversion completed.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FILE_CHOOSER && resultCode == getActivity().RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
                if (data != null && data.getData() != null) {
                    selectedFileUri = data.getData();
                    String selectedFilePath = getFilePathFromUri(selectedFileUri);
                    Toast.makeText(getActivity(), "Selected file: " + selectedFilePath, Toast.LENGTH_SHORT).show();
                    convertTxtToDocx();
                }
            } else {
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFilePathFromUri(Uri uri) {
        String filePath = null;
        if (uri != null) {
            if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
                String documentId = DocumentsContract.getDocumentId(uri);
                if (documentId != null && documentId.startsWith("raw:")) {
                    filePath = documentId.replaceFirst("raw:", "");
                } else {
                    String authority = uri.getAuthority();
                    String documentPath = DocumentsContract.getDocumentId(uri);
                    String[] split = documentPath.split(":");
                    if (split.length >= 2 && "com.android.externalstorage.documents".equals(authority)) {
                        String type = split[0];
                        if ("primary".equalsIgnoreCase(type)) {
                            filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                        }
                    }
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                String[] projection = {MediaStore.Files.FileColumns.DATA};
                Cursor cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
                        filePath = cursor.getString(columnIndex);
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                filePath = uri.getPath();
            }
        }
        return filePath;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFileExplorer();
            } else {
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
