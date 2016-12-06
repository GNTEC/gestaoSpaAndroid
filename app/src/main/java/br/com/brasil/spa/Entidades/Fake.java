/*
package br.com.brasil.spa.Entidades;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by Ivan on 21/11/2016.
 *//*


public class Fake {

    public static List<Unidade> generateUnidade(){

        List<Unidade> unidades = new ArrayList<>();
        List<Filial> filiais = new ArrayList<>();

        for(int i = 0; i< 1; i++){

            Unidade u = new Unidade();
            u.setNomeEmpresa("Empresa "+i);
            u.setCodEmpresa(i+1);

            for(int j = 0; j<3; j++){

                Filial filial = new Filial();
            filial.setCodFilial(j);
            filial.setNomeFilial("Filial "+j);
            filiais.add(filial);
        }

            u.setLstFilial(filiais);
            unidades.add(u);
            filiais = new ArrayList<>();

        }
        return unidades;
    }

    public static List<Servicos> generateServicos(){

        List<Servicos> servicos = new ArrayList<>();

            for(int i = 0; i < 5; i++){

                Servicos s = new Servicos();

                s.setCodServico(i+1);
                s.setDesServico("servico " + i);
                s.setValServico(50.0);

                servicos.add(s);
            }
        return servicos;
    }

    public static List<Profissionais> generateProfissionais(){

        List<Profissionais> profissional = new ArrayList<>();

            for(int i = 0; i < 5; i++){

                Profissionais p = new Profissionais();

                p.setCodProfissional(i+1);
                p.setNomeProfissional("Profissional " + i);
                p.setDataProfissional("22/11/2016");

                profissional.add(p);

            }
        return profissional;
    }

    public static List<HorasProfissional> generateHorasFuncionario(){

        List<HorasProfissional> horas = new ArrayList<>();

            for(int i = 0; i < 5; i++){

                HorasProfissional h = new HorasProfissional();
                h.setHoraInicio(String.valueOf(i+12 + ":" + "00"));
                h.setHoraTermino(String.valueOf(i+13 + ":" + "00"));
                horas.add(h);
            }

        return horas;
    }
}
*/
