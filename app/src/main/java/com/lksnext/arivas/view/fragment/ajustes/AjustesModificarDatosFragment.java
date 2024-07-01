package com.lksnext.arivas.view.fragment.ajustes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lksnext.arivas.R;

import java.util.HashMap;
import java.util.Map;

public class AjustesModificarDatosFragment extends Fragment {

    private NavController navController;
    private FirebaseFirestore db;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseUser currentUser;
    private TextView tvNombre;
    private TextView tvApellidos;
    private TextView tvCorreo;
    private TextView tvNumTlfn;
    private ImageView photoImageView;
    private TextView botonCambiarFoto;
    private StorageReference storageRef;
    private FirebaseStorage storage;

    private ListenerRegistration userListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ajustes_modificar_datos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        db = FirebaseFirestore.getInstance();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        photoImageView = view.findViewById(R.id.photoImageView);
        botonCambiarFoto = view.findViewById(R.id.btnCambiarFoto);

        tvNombre = view.findViewById(R.id.tvNombreValue);
        tvApellidos = view.findViewById(R.id.tvApellidosValue);
        tvCorreo = view.findViewById(R.id.tvCorreoValue);
        tvNumTlfn = view.findViewById(R.id.tvNumTlfnValue);

        tvCorreo = view.findViewById(R.id.tvCorreoValue);
        if (currentUser != null) {
            tvCorreo.setText(currentUser.getEmail());
        }

        loadUserData();

        navController = Navigation.findNavController(view);

        setupEditClickListener(view.findViewById(R.id.ivEditNombre), tvNombre);
        setupEditClickListener(view.findViewById(R.id.ivEditApellidos), tvApellidos);
        setupEditClickListener(view.findViewById(R.id.ivEditNumTlfn), tvNumTlfn);

        botonCambiarFoto.setVisibility(View.GONE);

        botonCambiarFoto.setOnClickListener(v -> {
            openFileChooser();
        });

        view.findViewById(R.id.btnConfirmarCambios).setOnClickListener(v -> {
            updateUserData();
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImage(imageUri);
        }
    }

    private void uploadImage(Uri imageUri) {
        if (currentUser != null) {
            StorageReference fileReference = storageRef.child("profile_images")
                    .child(currentUser.getUid() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            updateProfilePhoto(imageUrl);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void updateProfilePhoto(String imageUrl) {
        DocumentReference userRef = db.collection("users").document(currentUser.getUid());
        Map<String, Object> userData = new HashMap<>();
        userData.put("photoUrl", imageUrl);

        userRef.set(userData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "Foto de perfil actualizada", Toast.LENGTH_SHORT).show();
                    photoImageView.setImageURI(Uri.parse(imageUrl));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Error al actualizar la foto de perfil", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadUserData() {
        DocumentReference userRef = db.collection("users").document(currentUser.getUid());
        userListener = userRef.addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                Map<String, Object> userData = snapshot.getData();
                if (userData != null) {
                    tvNombre.setText(userData.get("nombre").toString());
                    tvApellidos.setText(userData.get("apellidos").toString());
                    tvNumTlfn.setText(userData.get("numTlfn").toString());
                }
            }
        });
    }

    private void setupEditClickListener(ImageView editImageView, TextView textViewToUpdate) {
        editImageView.setOnClickListener(v -> {
            String currentValue = textViewToUpdate.getText().toString();
            showEditDialog(currentValue, textViewToUpdate); // Mostrar el diálogo de edición
        });
    }

    private void showEditDialog(String currentValue, TextView textViewToUpdate) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_value, null);
        EditText editText = dialogView.findViewById(R.id.editTextValue);
        editText.setText(currentValue);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Editar Valor")
                .setView(dialogView)
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    String newValue = editText.getText().toString().trim();
                    textViewToUpdate.setText(newValue);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void updateUserData() {
        String nombre = tvNombre.getText().toString();
        String apellidos = tvApellidos.getText().toString();
        String numTlfn = tvNumTlfn.getText().toString();

        String uid = currentUser.getUid();

        DocumentReference userRef = db.collection("users").document(uid);

        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", uid);
        userData.put("nombre", nombre);
        userData.put("apellidos", apellidos);
        userData.put("numTlfn", numTlfn);

        userRef.set(userData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    navController.popBackStack(R.id.ajustesFragment, false);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Error al actualizar los datos", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (userListener != null) {
            userListener.remove();
        }
    }
}
