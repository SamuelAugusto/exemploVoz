package com.example.exemplovoz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private EditText edTexto;
    private Button btOuvir, btFalar;
    private ListView lstv;
    private TextToSpeech textoFala; //objeto utilizado para trabalhar com voz
    private Locale locale; //objeto que irá determinar qual idioma irá ser o padrão

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textoFala = new TextToSpeech(MainActivity.this, MainActivity.this);
        edTexto = (EditText)findViewById(R.id.edtTexto);
        botoes();
    }

    public void botoes()
    {
        btFalar = (Button)findViewById(R.id.btnFalar);
        btFalar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = edTexto.getText().toString();
                textoFala.speak(texto, TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY,"1");
            }
        });

        btOuvir=(Button)findViewById(R.id.btnOuvir);
        btOuvir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstv=(ListView)findViewById(R.id.lstPalavras);
                PackageManager pm = getPackageManager();
                List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0);
                if(activities.size() !=0){
                    Intent intent = getRocognizerIntent();
                    startActivityForResult(intent,0);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sem reconhecimento de voz", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onInit(int status) {
        locale = new Locale("pt", "BR");
        //textoFala.setLanguage(Locale.ENGLISH)
        textoFala.setLanguage(locale);
    }

    protected Intent getRocognizerIntent(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale aqui");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, "10");
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ArrayList<String> palavras = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            lstv.setAdapter(new ArrayAdapter<String>
                    (this, android.R.layout.simple_list_item_1, palavras));
        }
    }
}
