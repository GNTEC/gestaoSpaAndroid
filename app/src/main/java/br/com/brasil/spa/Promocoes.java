package br.com.brasil.spa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import br.com.brasil.spa.Utils.Sessao;

public class Promocoes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String resultado;
    private Integer filial;
    private Integer COD_EMPRESA;
    private TextView txv_promo_2;
    private String promocao;

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
        setContentView(R.layout.activity_promocoes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);

        }
        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.mobile_nav);
        upArrow.setColorFilter(Color.parseColor("#d0d0d0"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("Promoções");
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


        //EventBus.getDefault().register(this);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_promo);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("PROMOÇÕES");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);*/

        txv_promo_2 = (TextView) findViewById(R.id.txv_promo_2);

        filial = Sessao.getCodFilial();
        COD_EMPRESA = Sessao.COD_EMPRESA;

        getPromocoes();

    }


    public void getPromocoes() {
        new Thread() {
            @Override
            public void run() {
                try {

                    String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/GET_PROMOCOES_2";
                    String OPERATION_NAME = "GET_PROMOCOES_2";
                    String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
                    String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";
                    //String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao_2.asmx";

                    WebService objWs = new WebService();
                    objWs.setSOAP_ACTION(SOAP_ACTION);
                    objWs.setOPERATION_NAME(OPERATION_NAME);
                    objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
                    objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

                    String resultadPromocoes = objWs.getPromocoes(COD_EMPRESA, filial);

                    //JSONObject obj = new JSONObject(resultadPromocoes);
                    promocao =resultadPromocoes.replaceAll("\"", "");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            txv_promo_2.setText(promocao);

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
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
