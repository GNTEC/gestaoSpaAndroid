
package br.com.brasil.spa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import br.com.brasil.spa.Utils.Sessao;

public class Localizacao extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private List<Address> list;
    private Geocoder geocoder;
    private String endereco;
    private Double latitude;
    private Double longitude;
    private String result;

    //toolbar
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private ProgressDialog progressDialog;

    //header
    private NavigationView navigationView;
    private TextView txv_header_nome;
    private TextView txv_header_email;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);

        }
        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.mobile_nav);
        upArrow.setColorFilter(Color.parseColor("#d0d0d0"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("Localização");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4DB6AC")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //header navigation view
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View v = navigationView.getHeaderView(0);
        txv_header_nome = (TextView) v.findViewById(R.id.txv_header_nome);
        txv_header_email = (TextView) v.findViewById(R.id.txv_header_email);
        txv_header_nome.setText(Sessao.getNomeCliente());
        txv_header_email.setText(Sessao.getEMAIL());
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            finish();
            Intent intent = new Intent(this, Historico.class);
            startActivity(intent);
        } else if (id == R.id.nav_agendar) {
            finish();
            Intent intent = new Intent(this, MenuInicial.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            finish();
            Intent intent = new Intent(this, Localizacao.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            finish();
            Intent intent = new Intent(this, Promocoes.class);
            startActivity(intent);
        } else if (id == R.id.nav_unidade) {
            finish();
            Intent intent = new Intent(this, SelecaoUnidade.class);
            startActivity(intent);
        } else if (id == R.id.nav_la) {
            finish();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}




