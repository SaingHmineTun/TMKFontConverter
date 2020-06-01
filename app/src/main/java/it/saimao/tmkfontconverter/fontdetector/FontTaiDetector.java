package it.saimao.tmkfontconverter.fontdetector;

import android.content.Context;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

import it.saimao.tmkfontconverter.R;

public class FontTaiDetector {


    private Context context;
    private String input;

    public FontTaiDetector(Context context, String input) {
        this.context = context;
        this.input = input;
    }


    public double detectFont() {
        return predict(getMaps(R.raw.detect_font));
    }


    private Map<Integer, Integer> getMaps(int resource_id) {

        Map<Integer, Integer> maps = new HashMap<>();
        try {
            InputStream is = context.getResources().openRawResource(resource_id);
            ObjectInputStream ois = new ObjectInputStream(is);
            maps = (Map<Integer, Integer>) ois.readObject();
            ois.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return maps;

    }

    private double predict(Map<Integer, Integer> maps) {

        double detectFontLength = 0;
//        input = edInput.getText().toString();
        if (input.length() > 100) {
            detectFontLength = 100;
        } else {
            detectFontLength = input.length() - 1;
        }

        double total = 0;
        for (int i = 0; i < detectFontLength; i++) {
            int code = input.charAt(i);
//            System.out.println(code);
//            total += maps.get(code);
            if (maps.get(code) != null) {
                System.out.println(maps.get(code));
                total += maps.get(code);
            }
        }

//        String result = total / detectFontLength >= 0 ? "Unicode" : "Zawgyi";

        return total / detectFontLength;

    }


}
