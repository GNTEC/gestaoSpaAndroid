
package br.com.brasil.spa;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.brasil.spa.Utils.Eventos;
import br.com.brasil.spa.Utils.Sessao;

/**
 * Created by Anderson on 29/10/2016.
 */

public class Localizacao extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private List<Address> list;
    private Geocoder geocoder;
    private String endereco;
    private Double latitude;
    private Double longitude;
    private String result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao);

        geocoder = new Geocoder(this, Locale.getDefault());

        endereco = Sessao.getENDERECO();

        if(!endereco.equals(null)) {

            CoordinateConverter(endereco);
        }else{
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Unidade sem endereco cadastrado, reporte o administrador do sistema.");
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(Localizacao.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_localizacao);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("LOCALIZAÇÃO");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



    }

    public String CoordinateConverter(String adresss) {

        new Thread(){
            @Override
            public void run(){
                try{

                    list = (ArrayList<Address>) geocoder.getFromLocationName(endereco, 1);

                } catch (IOException e){
                    e.printStackTrace();
                } catch (IllegalArgumentException ex){
                    ex.printStackTrace();
                }

                if (list != null && list.size() > 0){

                    Address a = list.get(0);
                    latitude = a.getLatitude();
                    longitude = a.getLongitude();
                    result = String.valueOf(a.getLatitude() + ", " + a.getLongitude());
                }

             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     addMap();
                 }
             });
            }

        }.start();
        return result;
    }

    public void addMap(){
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-23.5434022, -46.6647113);
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("SPA Dona Beleza"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
    }
}




