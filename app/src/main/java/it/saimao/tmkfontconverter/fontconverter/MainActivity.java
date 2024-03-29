package it.saimao.tmkfontconverter.fontconverter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import it.saimao.tmkfontconverter.R;
import it.saimao.tmkfontconverter.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Typeface zgTypeface, uniTypeface;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set default typeface for 2 EditText
        zgTypeface = Typeface.createFromAsset(getAssets(), "fonts/zawgyi.ttf");
        uniTypeface = Typeface.createFromAsset(getAssets(), "fonts/unicode.ttf");
        binding.edInput.setTypeface(uniTypeface);
        binding.edOutput.setTypeface(zgTypeface);

        // Listen action
        binding.rbUni2Zg.setOnCheckedChangeListener(this);
        binding.rbZg2Uni.setOnCheckedChangeListener(this);

        binding.btConvert.setOnClickListener(this::convert);
        binding.btCopy.setOnClickListener(this::copy);
        binding.btCopy.setOnLongClickListener(this::copyBoth);
        binding.btFix.setOnClickListener(this::fix);
        binding.btClear.setOnClickListener(this::clear);
    }

    private void fix(View view) {
        if (binding.rbZg2Uni.isChecked()) {
            String input = binding.edInput.getText().toString();
            input = ShanZawgyiConverter.uni2zg(input);
            input = ShanZawgyiConverter.zg2uni(input);
            binding.edOutput.setText(input);
        } else {
            Toast.makeText(this, "Only work in converting Zawgyi to Unicode", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mi = menu.add(0, 0, 0, "About Us");
        mi.setIcon(R.drawable.about);
        mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == 0) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void clear(View view) {
        binding.edInput.getText().clear();
        binding.edOutput.getText().clear();
    }

    private void convert(View view) {
        String result;
        String input = binding.edInput.getText().toString();
        if (!input.isEmpty()) {
            if (binding.rbZg2Uni.isChecked()) {
                binding.edOutput.setTypeface(uniTypeface);
                result = ShanZawgyiConverter.zg2uni(input);
            } else {
                binding.edOutput.setTypeface(zgTypeface);
                result = ShanZawgyiConverter.uni2zg(input);
            }
            binding.edOutput.setText(result);
        }
    }


    public void copy(View view) {
        String text = binding.edOutput.getText().toString();
        if (text.isEmpty()) {
            Toast.makeText(this, "No Output Text to be Copied!!", Toast.LENGTH_SHORT).show();
            return;
        }
        copyToClipboard(text);
        Toast.makeText(this, "Output Text Copied Successfully!", Toast.LENGTH_SHORT).show();
    }

    // On Copy Long Click
    public boolean copyBoth(View view) {
        String outputText = binding.edOutput.getText().toString();
        String inputText = binding.edInput.getText().toString();
        if (outputText.isEmpty()) {
            Toast.makeText(this, "No Output Text to be Copied!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        String text;
        if (binding.rbZg2Uni.isChecked()) {
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (binding.rbUni2Zg.isChecked()) {
            binding.edInput.setTypeface(uniTypeface);
            binding.edInputLayout.setHint("Unicode");
            binding.edOutput.setTypeface(zgTypeface);
            binding.edOutputLayout.setHint("Zawgyi");
        } else {
            binding.edInput.setTypeface(zgTypeface);
            binding.edInputLayout.setHint("Zawgyi");
            binding.edOutput.setTypeface(uniTypeface);
            binding.edOutputLayout.setHint("Unicode");
        }

    }
}
