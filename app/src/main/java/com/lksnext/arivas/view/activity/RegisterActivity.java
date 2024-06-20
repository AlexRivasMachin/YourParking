package com.lksnext.arivas.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.lksnext.arivas.databinding.ActivityRegisterBinding;
import com.lksnext.arivas.viewmodel.login.RegisterViewModel;

public class    RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Asignamos la vista/interfaz de registro
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Asignamos el viewModel de register
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding.btnRegister.setOnClickListener(v -> {
           if (binding.mailInputLayout.getEditText() != null && binding.ContraseAInputLayout.getEditText() != null) {
               String email = binding.mailInputLayout.getEditText().getText().toString();
               String password = binding.ContraseAInputLayout.getEditText().getText().toString();

               FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                       .addOnCompleteListener(task -> {
                           if (task.isSuccessful()) {
                               Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                               startActivity(intent);
                           }
                            else {
                                 showAlertDialog("Error al registrar el usuario");
                            }
                       });
           }

        });

        binding.iniciarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void showAlertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
