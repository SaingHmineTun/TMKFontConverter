package it.saimao.tmkfontconverter.fontconverter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.saimao.tmkfontconverter.R;
import it.saimao.tmkfontconverter.fontdetector.ZawgyiDetector;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    public EditText edInput;
    public EditText edOutput;
    public TextInputLayout edInputLayout, edOutputLayout;
    private ZawgyiDetector zawgyiDetector;
    private String input = "";
    private boolean isZawgyi;
    private Typeface zgTypeface, uniTypeface;
    private MaterialButton btCopy;
    private RadioButton rbZg2Uni, rbUni2Zg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        edInput = findViewById(R.id.edInput);
        edInputLayout = findViewById(R.id.edInputLayout);
        edOutput = findViewById(R.id.edOutput);
        edOutputLayout = findViewById(R.id.edOutputLayout);
        rbZg2Uni = findViewById(R.id.rbZg2Uni);
        rbUni2Zg = findViewById(R.id.rbUni2Zg);
        rbUni2Zg.setOnClickListener(this);
        rbZg2Uni.setOnClickListener(this);
        zgTypeface = Typeface.createFromAsset(getAssets(), "fonts/zawgyi.ttf");
        uniTypeface = Typeface.createFromAsset(getAssets(), "fonts/unicode.ttf");
        edInput.setTypeface(uniTypeface);
        edOutput.setTypeface(zgTypeface);
        zawgyiDetector = new ZawgyiDetector(this);
        btCopy = findViewById(R.id.btCopy);
        btCopy.setOnLongClickListener(this);
        edInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String result = "";
                input = edInput.getText().toString();
                Log.d("input", rbZg2Uni.isChecked() + "");
                if (rbZg2Uni.isChecked()) {
                    result = ZawgyiConverter.zg2uni(input);
                } else if (rbUni2Zg.isChecked()) {
                    result = ZawgyiConverter.uni2zg(input);
                }
                edOutput.setText(result);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem1 = menu.add(0, 0, 0, "Clear");
        menuItem1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(1, 1, 1, "Credit");
        menu.add(2, 2, 2, "About Us");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == 0) {
            edOutput.setText("");
            edInput.setText("");
        } else if (item.getItemId() == 1) {

            showCreditDialog();

        } else if (item.getItemId() == 2) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCreditDialog() {


        // Mao Custom Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.mao_dialog, null, false);
        builder.setView(dialogView);

        final AlertDialog alert = builder.create();
//        alerts.add(alert);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        } else {
            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }
        alert.show();

        final Button btnClose = alert.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

    }

    private String convert(String input) {
        String result;
        this.input = input;
        isZawgyi = zawgyiDetector.isZawgyi(edInput.getText().toString());
        if (isZawgyi) {
            edOutput.setTypeface(uniTypeface);
            result = ZawgyiConverter.zg2uni(input);
        } else {
            edOutput.setTypeface(zgTypeface);
            result = ZawgyiConverter.uni2zg(input);
        }
        return result;
    }


    public void copy(View view) {
        String text = edOutput.getText().toString();
        if (text.equals("")) {
            Toast.makeText(this, "No Output Text to be Copied!!", Toast.LENGTH_SHORT).show();
            return;
        }
        copyToClipboard(text);
        Toast.makeText(this, "Output Text Copied Successfully!", Toast.LENGTH_SHORT).show();
    }

    // On Copy Long Click
    public boolean onLongClick(View view) {
        isZawgyi = zawgyiDetector.isZawgyi(edInput.getText().toString());
        String outputText = edOutput.getText().toString();
        String inputText = edInput.getText().toString();
        if (outputText.equals("")) {
            Toast.makeText(this, "No Output Text to be Copied!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        String text;
        if (isZawgyi) {
            text = "#Unicode#\n" + outputText + "\n#Zawgyi#\n" + inputText;
        } else {
            text = "#Unicode#\n" + inputText + "\n#Zawgyi#\n" + outputText;
        }
        copyToClipboard(text);
        Toast.makeText(this, "Unicode & Zawgyi Copied Successfully!", Toast.LENGTH_SHORT).show();

        return true;
    }

    private void copyToClipboard(String text) {

        ClipboardManager clipboard = (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

    public void convert(View view) {
        edOutput.setText(convert(edInput.getText().toString()));
    }

    public void fix(View view) {
        isZawgyi = zawgyiDetector.isZawgyi(edInput.getText().toString());
        if (isZawgyi) {
            edOutput.setText(ZawgyiConverter.zg2uni(ZawgyiConverter.uni2zg(input)));
        }
    }

    public void clickClear(View view) {
        edInput.setText("");
        edOutput.setText("");
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rbUni2Zg) {
            edInput.setTypeface(uniTypeface);
            edInputLayout.setHint("Unicode");
            edOutputLayout.setHint("Zawgyi");
            edOutput.setTypeface(zgTypeface);
        } else if (view.getId() == R.id.rbZg2Uni) {
            edOutput.setTypeface(uniTypeface);
            edOutputLayout.setHint("Unicode");
            edInputLayout.setHint("Zawgyi");
            edInput.setTypeface(zgTypeface);
        }
    }
}
