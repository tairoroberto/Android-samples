package br.com.trmasolucoes.testedialogfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by tairo on 17/07/15.
 */
public class CustomDiaolgFragment extends DialogFragment{

    private int numStyle;
    private int numTheme;

    public CustomDiaolgFragment(int numStyle, int numTheme) {
        this.numStyle = numStyle;
        this.numTheme = numTheme;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Script", "OnCreate");

        int style;
        int theme;

        switch (numStyle){
            case 1:
                style = DialogFragment.STYLE_NO_TITLE;
                break;
            case 2: style = DialogFragment.STYLE_NO_INPUT;
                break;
            case 3: style =- DialogFragment.STYLE_NO_FRAME;
                default:
                    style = DialogFragment.STYLE_NORMAL;
        }

        switch (numTheme){
            case 1:
                theme = android.R.style.Theme_Holo;
                break;
            case 2: theme = android.R.style.Theme_Holo_Dialog;
                break;
            case 3: theme = android.R.style.Theme_Holo_Light_DarkActionBar;
                break;
            default: theme = android.R.style.Theme_Holo_Light_Dialog;
        }

        setStyle(style, theme);
        setCancelable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.i("Script","OncrateView");

        View view = inflater.inflate(R.layout.dialog_fragment_layout,container);
        View view2 = inflater.inflate(R.layout.loading_layout,container);

        /*Button btnSair = (Button) view.findViewById(R.id.btnSair);

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss();
                ((MainActivity) getActivity()).turnOffDialog();
            }
        });
*/
        return view2;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Script","onActivityCreated");

        //pega o dialog e deixa transparente
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i("Script","OnAttach");
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.i("Script","onCancel");
    }

    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Log.i("Script", "OnCreateDialog");

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity())
                .setTitle("DialogFragment")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"OK pressed",Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"Cancel pressed",Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });

        return alert.show();
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("Script","onDetach");
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        Log.i("Script","onDestroyOptionsMenu");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.i("Script","onDimiss");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("Script","onSaveStanceState");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Script","onStart");
    }
}
