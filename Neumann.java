public class Neumann {


    static public double[][] koeff_a_for_ring_task(double[][] koeff_cond_in_polar, double r_1, double r_2, boolean second_cond) {
        double[][] koeff_a_and_b = new double[2][koeff_cond_in_polar[0].length];
        //if (second_cond == false) {//то есть находим коэфф для первого условия
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < koeff_cond_in_polar[0].length; j++) {
                if (koeff_cond_in_polar[i][j] !=0 ) {
                    if (j == 0) {
                        koeff_a_and_b[0][j] =  koeff_cond_in_polar[0][0] / (2 * (r_2 - r_1));
                    }
                    else {
                        koeff_a_and_b[i][j] = (r_2 - r_1) / (j + 1) * koeff_cond_in_polar[i][j];
                    }
                }
                System.out.print(koeff_a_and_b[i][j]+"  ");
            }
            System.out.println();
        }
        //}
        return koeff_a_and_b;
    }

    static public String print_res_for_ring_task(double[][] koeff_a_and_b, double r_1, double r_2, boolean second_cond){
        String res = "";
        String ro = "(" + r_2 + " - r)^";
        if (second_cond == true) {
            ro = "(r - " + r_1 + ")^";
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < koeff_a_and_b[0].length; j++) {
                if (koeff_a_and_b[i][j] !=0 ) {
                    if (i == 0) {
                        if (j == 0) {
                            res += "(" + koeff_a_and_b[i][j] + ") * " + ro + "2 + ";
                        } else if (j != koeff_a_and_b[0].length - 1) {
                            res += "(" + koeff_a_and_b[i][j] / Math.pow((r_2 - r_1), j + 1) + ") * " + ro + (j + 1) + " * cos(" + j + " * phi) + ";
                        } else {
                            res += "(" + koeff_a_and_b[i][j] / Math.pow((r_2 - r_1), j + 1) + ") * " + ro + (j + 1) + " * cos(" + j + " * phi)";
                        }
                    } else if (i == 1) {
                        if (j == 0) {
                            break;
                        }
                        else {
                            res += " + (" + koeff_a_and_b[i][j] / Math.pow((r_2 - r_1), j + 1) + ") * " + ro + (j + 1) + " * sin(" + j + " * phi)";
                        }
                    }
                }
            }
        }
        return res;
    }

    static public double[][] koeff_a_for_non_ring_task(double[][] koeff_cond_in_polar, double r) {
        double[][] koeff_a_and_b = new double[2][koeff_cond_in_polar[0].length];
        System.out.println("Коэфф а и в для внутренней/внешней задачи");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < koeff_cond_in_polar[0].length; j++) {
                if (koeff_cond_in_polar[i][j] !=0 ) {
                    koeff_a_and_b[i][j] = r/j*koeff_cond_in_polar[i][j];
                }
                System.out.print(koeff_a_and_b[i][j]+"  ");
            }
            System.out.println();
        }
        return koeff_a_and_b;
    }

    static public double[][] internal_task(double[][] koeff_a_and_b, double r) {//ВНУТРЕНЯЯ ЗАДАЧА
        System.out.println("Коэфф а и в после деления на соотв радиус в степени для внутренней задачи");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < koeff_a_and_b[0].length; j++) {
                if (koeff_a_and_b[i][j] !=0 ) {
                    koeff_a_and_b[i][j] *= Math.pow(r,-j);
                }
                System.out.print(koeff_a_and_b[i][j]+"  ");
            }
            System.out.println();
        }
        return koeff_a_and_b;
    }

    static public double[][] external_task(double[][] koeff_a_and_b, double r) {//ВНЕШНЯЯ ЗАДАЧА
        System.out.println("Коэфф а и в после умножения на соотв радиус в степени для внешней задачи");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < koeff_a_and_b[0].length; j++) {
                if (koeff_a_and_b[i][j] !=0 ) {
                    koeff_a_and_b[i][j] *= Math.pow(r,j);
                }
                System.out.print(koeff_a_and_b[i][j]+"  ");
            }
            System.out.println();
        }
        return koeff_a_and_b;
    }

    static public String print_res_for_external(double[][] new_koeff) {
        String res = "";
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < new_koeff[0].length; j++) {
                if (new_koeff[i][j] !=0 ) {
                    if (i == 0) {
                        if (j != new_koeff[0].length-1) {
                            res += "(" + new_koeff[i][j] + ") / r^" + j + " * cos(" + j + " * phi) + ";
                        }
                        else {
                            res += "(" + new_koeff[i][j] + ") / r^" + j + " * cos(" + j + " * phi)";
                        }
                    }
                    else {
                        res += " + (" + new_koeff[i][j] + ") / r^" + j + " * sin(" + j + " * phi)";
                    }
                }
            }
        }
        //System.out.println(res);
        return res;
    }

    static public String print_res_for_internal(double[][] new_koeff) {
        String res = "";
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < new_koeff[0].length; j++) {
                if (new_koeff[i][j] !=0 ) {
                    if (i == 0) {
                        if (j != new_koeff[0].length-1) {
                            res += "(" + new_koeff[i][j] + ") * r^" + j + " * cos(" + j + " * phi) + ";
                        }
                        else {
                            res += "(" + new_koeff[i][j] + ") * r^" + j + " * cos(" + j + " * phi)";
                        }
                    }
                    else {
                        res += " + (" + new_koeff[i][j] + ") * r^" + j + " * sin(" + j + " * phi)";
                    }
                }
            }
        }
        //System.out.println(res);
        return res;
    }

}
