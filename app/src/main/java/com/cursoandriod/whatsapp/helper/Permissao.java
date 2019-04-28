package com.cursoandriod.whatsapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {
    public static boolean validaPermicoes(int requestCode, Activity activity, String [] permissoes){

        if (Build.VERSION.SDK_INT >=23){
            List<String> listaPermissoes = new ArrayList<>();

            for (String permissao: permissoes) {
              boolean validaPermissao =  ContextCompat.checkSelfPermission(activity, permissao )== PackageManager.PERMISSION_GRANTED;
              if(! validaPermissao) listaPermissoes.add(permissao);
            }
            //caso a lista esteja vazias não é nescessario solicitar permissão
            if (listaPermissoes.isEmpty()) return true;

                String[] novaPermissoes = new String[listaPermissoes.size()];
                listaPermissoes.toArray(novaPermissoes);

            //solicitar permissão
            ActivityCompat.requestPermissions(activity, novaPermissoes, requestCode);

        }
        return true;

    }

    }

