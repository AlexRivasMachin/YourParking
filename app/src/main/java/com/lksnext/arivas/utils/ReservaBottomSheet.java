package com.lksnext.arivas.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolvableVoidResult;
import com.google.android.gms.wallet.CreateWalletObjectsRequest;
import com.google.android.gms.wallet.LoyaltyWalletObject;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.WalletObjectsClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lksnext.arivas.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ReservaBottomSheet extends BottomSheetDialogFragment {

    String plaza;
    String tipoPlaza;
    String fecha;
    String horaEntrada;
    String horaSalida;
    TextView plazaText;
    TextView tipoPlazaText;
    TextView fechaText;
    TextView horaEntradaText;
    TextView horaSalidaText;
    ImageView tipoPlazaImage;
    ImageView tipiPLazaIcon;

    public ReservaBottomSheet() {
        // Constructor vacío
    }

    public ReservaBottomSheet(String plaza, String tipoPlaza, String fecha, String horaEntrada, String horaSalida) {
        this.plaza = plaza;
        this.tipoPlaza = tipoPlaza != null ? tipoPlaza : "STD";
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.reserva_bottom_sheet, container, false);

        plazaText= rootView.findViewById(R.id.reserva_plaza);
        tipoPlazaText= rootView.findViewById(R.id.reserva_tipo);
        fechaText = rootView.findViewById(R.id.reserva_fecha);
        horaEntradaText = rootView.findViewById(R.id.reserva_in);
        horaSalidaText = rootView.findViewById(R.id.reserva_out);
        tipoPlazaImage = rootView.findViewById(R.id.Bottom_sheet_type);
        tipiPLazaIcon = rootView.findViewById(R.id.reserva_tipo_icon);

        setImage(tipoPlazaImage, tipoPlaza);
        setIcon(tipiPLazaIcon, tipoPlaza);

        plazaText.setText("Número de plaza:   " + plaza);
        tipoPlazaText.setText("Tipo:   " + getTipoPlaza(tipoPlaza));
        fechaText.setText("Fecha de reserva:   " + fecha);
        horaEntradaText.setText("Hora de entrada:   " + horaEntrada);
        horaSalidaText.setText("Hora de salida:   " +horaSalida);

        rootView.findViewById(R.id.eliminar_reserva).setOnClickListener(v -> {
            createDeleteDialog(FirebaseAuth.getInstance().getCurrentUser().getUid());
        });

        rootView.findViewById(R.id.addToGoogleWalletButton).setOnClickListener(v -> {
            saveToGoogleWallet();
        });

        return rootView;
    }

    private void setIcon(ImageView tipiPLazaIcon, String tipoPlaza) {
        switch (tipoPlaza) {
            case "STD":
                tipiPLazaIcon.setImageResource(R.drawable.auto);
                break;
            case "MOTO":
                tipiPLazaIcon.setImageResource(R.drawable.motociclea);
                break;
            case "ELEC":
                tipiPLazaIcon.setImageResource(R.drawable.electrico);
                break;
            case "DISC":
                tipiPLazaIcon.setImageResource(R.drawable.discapacitado);
                break;
            default:
                tipiPLazaIcon.setImageResource(R.drawable.auto);
                break;
        }
    }

    private void setImage(ImageView tipoPlazaImage, String tipoPlaza) {
        switch (tipoPlaza) {
            case "STD":
                tipoPlazaImage.setImageResource(R.drawable.coche_normal);
                break;
            case "MOTO":
                tipoPlazaImage.setImageResource(R.drawable.moto);
                break;
            case "ELEC":
                tipoPlazaImage.setImageResource(R.drawable.coche_electrico);
                break;
            case "DISC":
                tipoPlazaImage.setImageResource(R.drawable.coche_minusvalido);
                break;
            default:
                tipoPlazaImage.setImageResource(R.drawable.coche_normal);
                break;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public String getTipoPlaza(String tipoPlaza){
        switch (tipoPlaza) {
            case "STD":
                return "Plaza estándar";
            case "MOTO":
                return "Plaza de moto";
            case "ELEC":
                return "Plaza con estación de carga";
            case "DISC":
                return "Plaza para minusválidos";
            default:
                return "Plaza estándar";
        }
    }

    public void createDeleteDialog(String uid) {
        new MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.delete_forever)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que quieres eliminar la reserva?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    deleteReservation(uid, tipoPlaza, fecha, horaEntrada);
                })
                .show();
    }

    public void deleteReservation(String uid, String slotType, String date, String startTime) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("reservations")
                .whereEqualTo("uid", uid)
                .whereEqualTo("type", slotType)
                .whereEqualTo("date", date)
                .whereEqualTo("in", startTime)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        db.collection("reservations")
                                .document(document.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "Error al eliminar la reserva: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Error al buscar la reserva: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveToGoogleWallet() {
        LoyaltyWalletObject loyaltyObject = buildLoyaltyObject();

        WalletObjectsClient walletObjectsClient = Wallet.getWalletObjectsClient(requireActivity(), new Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .build());

        CreateWalletObjectsRequest.Builder requestBuilder = CreateWalletObjectsRequest.newBuilder();
        requestBuilder.setLoyaltyWalletObject(loyaltyObject);
        CreateWalletObjectsRequest request = requestBuilder.build();

        Task<AutoResolvableVoidResult> task = walletObjectsClient.createWalletObjects(request);

        task.addOnSuccessListener(autoResolvableVoidResult -> {
            Toast.makeText(requireContext(), "Pase añadido a Google Wallet", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        task.addOnFailureListener(e -> {
            Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private LoyaltyWalletObject buildLoyaltyObject() {
        LoyaltyWalletObject.Builder loyaltyObject = LoyaltyWalletObject.newBuilder();

        loyaltyObject.setProgramName("Parking Reserva");
        loyaltyObject.setAccountId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        loyaltyObject.setIssuerName("Parking Company");

        return loyaltyObject.build();
    }
}
