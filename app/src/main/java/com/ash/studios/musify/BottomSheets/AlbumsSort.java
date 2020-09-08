package com.ash.studios.musify.BottomSheets;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ash.studios.musify.Adapters.AlbumAdapter;
import com.ash.studios.musify.R;
import com.ash.studios.musify.Utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static android.content.Context.MODE_PRIVATE;
import static com.ash.studios.musify.Utils.Constants.ALBUMS_SORT;

public class AlbumsSort extends BottomSheetDialogFragment {
    RadioButton button0, button1, button2, button3, button4, button5;
    RadioGroup sortGroup;
    CheckBox reverse;
    RecyclerView rv;
    ImageView close;
    ProgressBar pb;
    TextView nf;

    Context context;
    SharedPreferences prefs;

    public AlbumsSort(Context context, RecyclerView rv, ProgressBar pb, TextView nf) {
        this.context = context;
        this.nf = nf;
        this.pb = pb;
        this.rv = rv;

        prefs = context.getSharedPreferences(ALBUMS_SORT, MODE_PRIVATE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sort_sheet, container, false);

        setIDs(v);
        return v;
    }

    private void setIDs(View v) {
        close = v.findViewById(R.id.close);
        sortGroup = v.findViewById(R.id.sort_group);
        button0 = v.findViewById(R.id.by_track);
        button1 = v.findViewById(R.id.by_name);
        button2 = v.findViewById(R.id.by_title);
        button3 = v.findViewById(R.id.by_album);
        button4 = v.findViewById(R.id.by_artist);
        button5 = v.findViewById(R.id.by_year);
        reverse = v.findViewById(R.id.order_by_check);
        getCheckState();

        close.setOnClickListener(c -> dismiss());
        button0.setVisibility(View.GONE);
        button1.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                prefs.edit()
                        .putString("sort_by", "album")
                        .putBoolean("order_by", reverse.isChecked())
                        .apply();
                rv.setAdapter(new AlbumAdapter(context, Utils.getAlbums(context), pb, nf));
            }
        });
        button2.setVisibility(View.GONE);
        button3.setVisibility(View.GONE);
        button4.setVisibility(View.GONE);
        button5.setVisibility(View.GONE);
        reverse.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) prefs.edit().putBoolean("order_by", true).apply();
            else prefs.edit().putBoolean("order_by", false).apply();
            rv.setAdapter(new AlbumAdapter(context, Utils.getAlbums(context), pb, nf));
        });
    }

    private void getCheckState() {
        reverse.setChecked(prefs.getBoolean("order_by", false));
        if (prefs.getString("sort_by", "album").equals("album")) button1.setChecked(true);
    }
}