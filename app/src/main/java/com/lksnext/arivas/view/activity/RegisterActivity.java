package com.lksnext.arivas.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.lksnext.arivas.R;
import com.lksnext.arivas.databinding.ActivityRegisterBinding;
import com.lksnext.arivas.utils.ProviderType;
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

               if (email.isEmpty() || password.isEmpty()) {
                   showAlertDialog("Por favor, completa todos los campos.");
                   return;
               }

               FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                       .addOnCompleteListener(task -> {
                           if (task.isSuccessful()) {
                               SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
                               SharedPreferences.Editor editor = prefs.edit();
                               editor.putString("email", email);
                               editor.putString("provider", ProviderType.BASIC.name());
                               editor.apply();

                               // Ir a la actividad principal
                               Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
