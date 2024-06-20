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
import com.lksnext.arivas.databinding.ActivityLoginBinding;
import com.lksnext.arivas.viewmodel.login.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Asignamos la vista/interfaz login (layout)
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Asignamos el viewModel de login
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        //Acciones a realizar cuando el usuario clica el boton de login
        binding.iniciarSesion.setOnClickListener(v -> {
            if (binding.NombreUsuariolayout.getEditText() != null && binding.ContraseAInputLayout.getEditText() != null) {
                String email = binding.NombreUsuariolayout.getEditText().getText().toString();
                String password = binding.ContraseAInputLayout.getEditText().getText().toString();

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("email", email);
                                editor.putString("provider", ProviderType.BASIC.name());
                                editor.apply();

                                startActivity(intent);
                            }
                            else {
                                showAlertDialog("Error al registrar el usuario");
                            }
                        });
            }
        });

        //Acciones a realizar cuando el usuario clica el boton de crear cuenta (se cambia de pantalla)
        binding.registrarse.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        //Observamos la variable logged, la cual nos informara cuando el usuario intente hacer login y se
        //cambia de pantalla en caso de login correcto
        loginViewModel.isLogged().observe(this, logged -> {
            if (logged != null) {
                if (logged) {
                    //Login Correcto
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    //Login incorrecto
                }
            }
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