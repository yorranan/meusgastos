package com.example.meusgastos.common;

import java.text.SimpleDateFormat;
import java.util.Date;

// A classe comomm aplica aquilo que vai ser usado em vários locais (classes)
public class ConversorData {
    // o static indica que não precisa instanciar
    public static String converterDateParaDataHora(Date data) {
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        return formatador.format(data);
    }
}
