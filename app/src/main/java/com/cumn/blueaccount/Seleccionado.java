package com.cumn.blueaccount;


public class Seleccionado {
    public static class GlobalVariables {
        public static String myString;
        public static float valor=1;

        public static float getValor() {
            if (myString == null) {
                return 0.0f; // or some other default value
            }
            valor = Float.parseFloat(myString.substring(myString.indexOf(":") + 1));
            return valor;
        }

        public static void setValor(float valor) {
            GlobalVariables.valor = valor;
        }

        public static void setMyString(String myString) {
            GlobalVariables.myString = myString;
        }
    }

}