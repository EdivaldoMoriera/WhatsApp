package com.cursoandriod.whatsapp.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cursoandriod.whatsapp.R;
import com.cursoandriod.whatsapp.helper.Permissao;
import com.cursoandriod.whatsapp.helper.Preferencias;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;

import java.time.Instant;
import java.util.HashMap;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    private EditText nome;
    private EditText cod_pais;
    private  EditText cod_estado;
    private EditText telefone;
    private Button cadastrar;
    private String [] permissoesNescessarias = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.INTERNET

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissao.validaPermicoes(1,this,permissoesNescessarias );

        nome = (EditText)findViewById(R.id.edit_nome);
        cod_pais  = (EditText) findViewById(R.id.edit_cod_pais);
        cod_estado =(EditText) findViewById(R.id.edit_cod_estado) ;
        telefone = (EditText) findViewById(R.id.edit_telefone);
        cadastrar = (Button) findViewById(R.id.button_cadastrar);

        //definir mascara
      // SimpleMaskFormatter simpleMaskNome = new SimpleMaskFormatter("");
        SimpleMaskFormatter simpleMaskPais = new SimpleMaskFormatter("+NN");
        SimpleMaskFormatter simpleMaskEstado = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter simpleMaskTelefome = new SimpleMaskFormatter(" NNNNN-NNNN");

      // MaskTextWatcher maskNome = new MaskTextWatcher(nome, simpleMaskNome);
        MaskTextWatcher maskPais = new MaskTextWatcher(cod_pais, simpleMaskPais);
        MaskTextWatcher maskEstado = new MaskTextWatcher(cod_estado, simpleMaskEstado);
        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone, simpleMaskTelefome);

      //  nome.addTextChangedListener(maskNome);
        cod_pais.addTextChangedListener(maskPais);
        cod_estado.addTextChangedListener(maskEstado);
        telefone.addTextChangedListener(maskTelefone);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String nomeUsuario = nome.getText().toString();
            String telefoneCompleto =
                    cod_pais.getText().toString()
                    +cod_estado.getText().toString()
                    +telefone.getText().toString();
            String telefoneSemFormatacao = telefoneCompleto.replace("+", "");
            telefoneSemFormatacao = telefoneSemFormatacao.replace("-", "");

               // Log.i("TELEFONE", "T" +telefoneCompleto);
                //Gerar token

                Random randomico = new Random();
                int numeroRandomico = randomico.nextInt(9999 - 1000)+1000;
                String token = String.valueOf(numeroRandomico);
                String mensagemEnvio = "Whatsapp códido de formatacao" +token;
               // Log.i("Token", "T" +telefoneCompleto);

              //salvar os dados para validação

                Preferencias preferencias = new Preferencias(getApplicationContext());//LoginActivity.this
                preferencias.salvarUsuarioPreferencias(nomeUsuario, telefoneSemFormatacao, token);

                //ENVIO DO SMS

               boolean enviadoSMS =  envioSMS("+"+telefoneSemFormatacao, mensagemEnvio);

               if (enviadoSMS){
                   Intent intent  = new Intent(LoginActivity.this, ValidadorActivity.class);
                   startActivity(intent);
                   finish();

               }else {

                   Toast.makeText(LoginActivity.this, "Problema ao enviar SMS, tente novamente!!!",Toast.LENGTH_LONG).show();

               }

               /* HashMap<String,String> usuario = preferencias.getDadosUsuario();
               Log.i("TOKEN", "NOME: "+usuario.get("nome " )+"FONE"+usuario.get("telefone"));*/

            }
        });

    }
    private boolean envioSMS(String telefone, String mensagem){
        try {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissons, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissons,grantResults);
        for (int resutado: grantResults){
            if (resutado == PackageManager.PERMISSION_DENIED){

                alertaValidacaoPermissao();

            }

        }


    }
    //janela de dialogo

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissoões negadas");
        builder.setMessage("para utilizar esse app, é nescessario aceitar as permissões");
        builder.setPositiveButton("CONFIRMA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
