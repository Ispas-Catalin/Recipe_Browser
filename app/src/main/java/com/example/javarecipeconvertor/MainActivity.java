package com.example.javarecipeconvertor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CpuUsageInfo;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mainET;
    public String mainText, j, auxRecomopsed,oof;
    public String cant[], measure[], ingrd[], cutText[],mainTextAux[],RecomposedText[],Comparator[];
    public int m;
    public boolean formatok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainET = findViewById(R.id.mainET);


        findViewById(R.id.btt_nact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialogTextDetector();
            }
        });


    }


    public void OpenDialogTextDetector()  {

        if(emptyString())
        {
            Toast.makeText(this,"Please insert a recipe", Toast.LENGTH_LONG).show();
            return;
        }

        isformat();

        if(formatok == true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View view = getLayoutInflater().inflate(R.layout.detector_dialog, null);
            TextView AlertTV = view.findViewById(R.id.AlertTV);
            String AuxMainEt = (String) mainET.getText().toString();
            AlertTV.setText(FilterOf(AuxMainEt));


            builder.setView(view)
                    .setTitle("Detected Text")
                    .setPositiveButton("Convert", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SendData();
                        }
                    })
                    .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create().show();
        }
        else if (formatok == false)
        {
            int i=0;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View view = getLayoutInflater().inflate(R.layout.detector_dialog, null);
            TextView AlertTV = view.findViewById(R.id.AlertTV);
            AlertTV.setText(" Please insert a recipe of this format:" + '\n' +
                    "1 cup of flour" +'\n' +
                    "2 tablespoons of brown sugar" +'\n' +
                    "3 4/5 tsp. kosher salt");

            builder.setView(view)
                    .setTitle("Detected Text")
                    .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create().show();

        }

    } /// Porneste Dialog -> FiltruOF text -> Edit/SendData


    public void SendData() {
        Intent intent = new Intent(getBaseContext(), Activity2.class);
        intent.putExtra("Edit_Text", FilterOf(mainET.getText().toString()));
        intent.putExtra("Cant", cant);
        intent.putExtra("Measure", measure);
        intent.putExtra("Ingrd", ingrd);
        intent.putExtra("M",m);

        startActivity(intent);
    } /// Trimite Text -> Trimite text filtrat catre activitatea2


    public void isformat()/// Verifica ca formatul retetei introdus sa fie bun
    {
        TextDetect();
        mainText = FilterOf(mainET.getText().toString());
        mainText = mainText.replace("\n", " ");
        mainTextAux = mainText.split(" ");
        formatok=true;
        StringBuilder builder= new StringBuilder();
        for (String current : RecomposedText)
        {
            builder.append(current + " ");
        }
        auxRecomopsed = builder.toString();
        Comparator = auxRecomopsed.split(" ");
        int i=0;
        for (String current : mainTextAux)
        {
            if(Compare(current, Comparator[i]) == false)
                formatok = false;
            i++;
        }

    }

    public void TextDetect()/// Separa reteta introdusa in 3 array of strings - cant, measure, ingrd -
    {

        cant = new String [100];
        ingrd = new String[100];
        measure= new String[100];
        RecomposedText = new String[300];

        int okc = 1;
        int i = 0;
        j = FilterOf(mainET.getText().toString());
        j = j.replace("\n", " ");
        cutText = j.split(" ");

        for(String current : cutText)
        {
            if(iscant(current) == true)
            {
                if (okc == 1)
                {
                    i++;
                    cant[i] = current;
                    okc = 0;

                }
                else if (okc == 0)
                {
                    cant[i] = cant[i] + " " + current;

                }
            }
            else if (ismeasure(current) == true)
            {
                if (measure[i]!= null)
                {
                    measure[i] = measure[i] + " " + current;
                }
                else if (measure[i] == null)
                    measure[i] = current;
            }
            else if (iscant(current) == false && ismeasure(current) == false)
            {
                okc = 1;
                if(ingrd[i] != null)
                {
                    ingrd[i] = ingrd[i] + " " + current;
                }
                else if(ingrd[i] == null)
                {
                    ingrd[i] = current;
                }

            }

        }
        m = i;
        int k =0;
        for( i=1; i<=m; i++)
        {
            RecomposedText[k]=cant[i];
            k++;
            RecomposedText[k]=measure[i];
            k++;
            RecomposedText[k]=ingrd[i];
            k++;
        }

    }

    public static boolean iscant(String s) /// Verifica daca un String e cantitate
    {
        String aux = s;
        char [] CharCut = aux.toCharArray();
        boolean ok = false;
        for (char current : CharCut)
        {
            if(current == '0' || current =='1' || current == '2' || current =='3')
                ok = true;
            else if (current == '4' || current =='5' || current == '6' || current =='7')
                ok = true;
            else if (current == '8' || current =='9' || current == '/')
                ok = true;
        }

        return  ok;
    }

    public static  boolean ismeasure(String s) //// Verifica daca un String e measure
    {
        if (s.equals("tablespoon") || s.equals("tablespoons") || s.equals("tbsp") || s.equals("tbsp."))
            return true;
        if (s.equals("teaspoon") || s.equals("teaspoons") || s.equals("tsp") || s.equals("tsp."))
            return true;
        if (s.equals("cup") || s.equals("cups"))
            return true;
        else return false;
    }

    public static boolean Compare (String s1, String s2)
    {
        char [] CharCut1 = s1.toCharArray();
        char [] CharCut2 = s2.toCharArray();

        if( CharCut1.length != CharCut2.length)
        {
            return false;
        }
        else {
            for(int i=0; i<CharCut1.length; i++)
            {
                if(CharCut1[i]!= CharCut2[i])
                    return false;
            }
            return true;
        }
    }

    public String FilterOf(String PreFilterOf) {
        String[] RemoveOfArray = PreFilterOf.split(" ");
        StringBuilder builder = new StringBuilder();
        for(String current : RemoveOfArray)
        {
            if(!"of".equals(current))
                builder.append(current).append(" ");
        }
        return builder.toString();
    }/// Filtreaza toate cuvintele "of" din string

    public boolean emptyString()
    {
        return mainET.getText().toString().isEmpty();
    }



}

