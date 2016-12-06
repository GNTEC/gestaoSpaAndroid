package br.com.brasil.spa.Utils;

/**
 * Created by Ivan on 21/11/2016.
 */

public class Eventos {

    public static final class RecebeData{

        String data;

        public RecebeData(String data){
            this.data = data;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
