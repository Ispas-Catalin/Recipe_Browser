package com.example.javarecipeconvertor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Activity2 extends AppCompatActivity {

    private TextView EditTextTV;
    public String j;
    public String cant[], measure[], ingrd[];
    public int m;
    public double intcant[][];
    public double fractioncant[];
    public boolean okfilter;

    DecimalFormat measureformat = new DecimalFormat("###,###.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        EditTextTV = findViewById(R.id.EditTextTV);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        okfilter=true;

        if (b != null) {
            j = (String) b.get("Edit_Text");
            cant = (String[]) b.get("Cant");
            measure = (String[]) b.get("Measure");
            ingrd = (String[]) b.get("Ingrd");
            m = (int) b.get("M");
            EditTextTV.setText(j);
        }

        findViewById(R.id.filterbttn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Convert();
                BuildAndShow();
            }
        });

    }


    public  void Convert() /// Functie principala
    {
        if (okfilter == true) {
            mTransform();
            mSimplify();
            toCup();
        }
    }


    public  void mTransform () /// Transforma measure in fractie
    {
        int bpos = 0;
        int spos = 0;
        int cont =0;
        int nraux = 0;
        char[] charaux;
        charaux = new char[100];
        intcant = new double[3][100];
        for(int i=1;i<=m;i++)
        {
            charaux = cant[i].toCharArray();
            if(charaux.length>3)
            {
                for(int j=0; j<charaux.length; j++)
                {
                    if(charaux[j]=='/')
                    {
                        bpos=j;
                    }
                    if(charaux[j]==' ')
                    {
                        spos=j;
                    }
                }
                cont = 0;
                nraux = 0;
                for(int j=bpos-1; j>spos;j--)
                {
                    if(nraux!=0)
                    {
                        if(charaux[j]!=0)
                            nraux = nraux + powerof10(cont)*(charaux[j]-48);
                        else nraux = nraux + powerof10(cont);
                    }
                    else
                    {
                        if(charaux[j]!=0)
                            nraux = powerof10(cont)*(charaux[j]-48);
                        else nraux = powerof10(cont);
                    }
                    cont++;
                }
                intcant[1][i]=nraux;
                cont=0;
                nraux=0;
                for(int j=charaux.length-1; j>bpos;j--)
                {
                    if(nraux!=0)
                    {
                        if(charaux[j]!=0)
                            nraux = nraux + powerof10(cont)*(charaux[j]-48);
                        else nraux = nraux + powerof10(cont);
                    }
                    else
                    {
                        if(charaux[j]!=0)
                            nraux = powerof10(cont)*(charaux[j]-48);
                        else nraux = powerof10(cont);
                    }
                    cont++;

                }
                intcant[2][i]=nraux;
                cont=0;
                nraux=0;
                for(int j=spos-1; j>-1;j--)
                {
                    if(nraux!=0)
                    {
                        if(charaux[j]!=0)
                            nraux = nraux + powerof10(cont)*(charaux[j]-48);
                        else nraux = nraux + powerof10(cont);
                    }
                    else
                    {
                        if(charaux[j]!=0)
                            nraux = powerof10(cont)*(charaux[j]-48);
                        else nraux = powerof10(cont);
                    }
                    cont++;
                }
                intcant[1][i]=intcant[1][i] + nraux*intcant[2][i];
            }

            else
            {
                cont=0;
                nraux=0;
                for(int j=charaux.length-1; j>-1;j--)
                {
                    if(nraux!=0)
                    {
                        if(charaux[j]!=0)
                            nraux = nraux + powerof10(cont)*(charaux[j]-48);
                        else nraux = nraux + powerof10(cont);
                    }
                    else
                    {
                        if(charaux[j]!=0)
                            nraux = powerof10(cont)*(charaux[j]-48);
                        else nraux = powerof10(cont);
                    }
                    cont++;
                }
                intcant[1][i]=nraux;
                intcant[2][i]=1;
            }

        }
    }

    public void mSimplify() /// Transforma fractie in impartire
    {
        fractioncant = new double[100];
        for(int i=1 ; i<=m; i++)
        {
            StringBuilder builder = new StringBuilder();
            fractioncant[i] = intcant[1][i]/intcant[2][i];
        }
    }

    static int powerof10 (int n)
    {
        int aux=1;
        if(n==0)
            return 1;
        else
        {
            for(int i=1;i<=n;i++)
                aux=aux*10;
            return aux;
        }
    }

    public void toCup()
    {
        for(int i=1; i<=m; i++)
        {
            if (measure[i].equals("teaspoon") || measure[i].equals("teaspoons") || measure[i].equals("tsp") || measure[i].equals("tsp."))
            {
                fractioncant[i]= fractioncant[i]/48;
                measure[i]="cups";
            }
            if (measure[i].equals("tablespoon") || measure[i].equals("tablespoons") || measure[i].equals("tbsp") || measure[i].equals("tbsp."))
            {
                fractioncant[i] = fractioncant[i]/16;
                measure[i]="cups";
            }
        }
    }

    public void BuildAndShow ()
    {

        for(int i=1; i<=m; i++)
        {
            StringBuilder builder = new StringBuilder();
            builder.append(measureformat.format(fractioncant[i]));
            cant[i]=builder.toString();
        }


        StringBuilder sbuilder = new StringBuilder();
        for (int i = 1; i<=m; i++)
            sbuilder.append(cant[i]+ " " + measure[i]+ " " + ingrd[i]+ '\n');
        EditTextTV.setText(sbuilder.toString());
        okfilter=false;

    }



}


