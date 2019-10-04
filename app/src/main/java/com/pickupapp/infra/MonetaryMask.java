package com.pickupapp.infra;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;

public class MonetaryMask implements TextWatcher {
    private EditText campo;

    public MonetaryMask(EditText campo) {
        super();
        this.campo = campo;
    }

    private boolean isUpdating = false;
    private NumberFormat nf = NumberFormat.getCurrencyInstance();

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int after) {
        if (isUpdating) {
            isUpdating = false;
            return;
        }
        isUpdating = true;
        String str = s.toString();
        boolean hasMask = ((str.indexOf("R$") > -1 || str.indexOf('$') > -1) &&
                (str.indexOf('.') > -1 || str.indexOf(',') > -1));
        if (hasMask) {
            str = str.replaceAll("[R$]", "").replaceAll("[,]", "")
                    .replaceAll("[.]", "");
        }
        try {
            str = nf.format(Double.parseDouble(str) / 100);
            campo.setText(str);
            campo.setSelection(campo.getText().length());
        } catch (NumberFormatException ignored) {
            //error
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //nada há implementar aqui.
    }

    @Override
    public void afterTextChanged(Editable s) {
        //nada há implementar aqui.
    }
}
