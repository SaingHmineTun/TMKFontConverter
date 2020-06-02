package it.saimao.tmkfontconverter.fontconverter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import it.saimao.tmkfontconverter.R;

public class MaoAdapter extends BaseAdapter {

    Context context;
    String[] stringsAsk, stringsValues;
    int icons[];
    LayoutInflater inflater;
    private Typeface uniTypeface;


    public MaoAdapter(Context context, String[] stringsAsk, String[] stringsValues, int[] icons) {
        this.context = context;
        this.stringsAsk = stringsAsk;
        this.stringsValues = stringsValues;
        this.icons = icons;
        inflater = LayoutInflater.from(context);;
        uniTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/unicode.ttf");
    }

    @Override
    public int getCount() {
        return stringsValues.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_list_view, null);
        TextView txtAsk = view.findViewById(R.id.textAsk);
        TextView txtValue = view.findViewById(R.id.textValue);
        txtValue.setTypeface(uniTypeface);
        ImageView icon = view.findViewById(R.id.iconView);
        txtAsk.setText(stringsAsk[i]);
        txtValue.setText(stringsValues[i]);
        icon.setImageResource(icons[i]);
        return view;
    }
}