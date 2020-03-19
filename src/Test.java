import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
    public  static int Icode = 0,countX=0,countMinH=0,countEpsX=0, countMaxH=0;
   public static void Test(Double A,Double B,Double hmin,Double hmax,Double yc, Double eps) throws IOException {
      FileOutputStream fileOutputStream=new FileOutputStream(new File("result.txt"),true);
      Double hn=(B-A)/10;//начальный шаг
       Double xn=A, yn=yc,yp3,yp4=yc,localError;
        if(hn<hmin){//проверка начального шага, вдруг он меньше минимального и больше максимального
                        hn=hmin;
                        countMinH++;
                    }
         if(hn>hmax){
                        hn=hmax;
                        countMaxH++;
                    }
                fileOutputStream.write("x               y        localError\r\n".getBytes());
                String data=Double.toString(xn)+" " +Double.toString(yn)+" 0\r\n";
                fileOutputStream.write(data.getBytes());
                countX++;//записываем в файлик и не забываем считать точки
                while (xn<=B)
                    {
                        yp3=metod3p(xn,yn,hn);//вычисляем методами 3 и 4 порядков
                  yp4=metod4p(xn,yp4,hn);
                 localError=Math.abs(yp3-yp4);//находим локальную погрешность
                  hn=estimateStep(hn,hmin,hmax,localError,eps);//проверяем наш шаг
                         xn = xn + hn;
                         yn = yp3;
                      countX++;
                      data=Double.toString(xn)+" "+Double.toString(yn)+" "+Double.toString(localError)+"\r\n";
                           fileOutputStream.write(data.getBytes());

                 }
                System.out.println(Icode);
               data="Icode "+Integer.toString(Icode)+"\r\n";
                fileOutputStream.write(data.getBytes());
                data="Number x "+Integer.toString(countX)+"\r\n";
               fileOutputStream.write(data.getBytes());
               data="Number x with min step "+Integer.toString(countMinH)+"\r\n";
               fileOutputStream.write(data.getBytes());
               data="Number x with max step "+Integer.toString(countMaxH)+"\r\n";
             fileOutputStream.write(data.getBytes());
               data="Number x with big localError "+Integer.toString(countEpsX)+"\r\n";
               fileOutputStream.write(data.getBytes());
          };

     /*  public static Double checkEnd(Double B, Double xn, Double hn,Double hmin){
+        if(B-(xn+hn)<hmin) {
+            if(B-xn>=2*hmin){
+                checkEndFlag=2;
+                return B-hmin-xn;
+            }
+            else {
+                if (B - xn <= 1.5 * hmin) {
+                    checkEndFlag=1;
+                    return B-xn;
+                } else {
+                    checkEndFlag=2;
+                    return (B-xn)/2;
+                }
+            }
+        }
+        else {
+            return hn;
+        }
+
+
+    }*/
          //функция оценки шага методом выбора максимальной для заданной точности длины шага([1]стр 45)
            public static Double estimateStep(Double hn,Double hmin,Double hmax,Double localError,Double eps){
                if(localError>eps) {//если получившаяся погрешность больше заданной то выбираем шаг+
                     if (hn < hmin) {
                               hn = hmin;
                              countMinH++;
                          } else {
                             if (hn > hmax) {
                                        hn = hmax;
                                      countMaxH++;
                                   } else {
                                      hn = 0.9 * Math.sqrt(Math.sqrt(eps / Math.abs(localError)))*hn;
                                   }
                          }
                      countEpsX++;
                       Icode=1;//заданная точность не была достигнута
                   }
               return hn;
           }
    public static Double f(Double x, Double y){
               return 12*x*x;
           };

            //метод рунге кутта ([1] формула 110)
           public static Double metod3p(Double xn, Double yn, Double hn){
                Double k1,k2,k3,ynext;
               k1 = hn * f(xn, yn);
                k2 = hn * f(xn + hn/3, yn + k1 / 3);
               k3=hn*f(xn+2*hn/3,yn+2*k2/3);

                      ynext=yn+(k1+3*k3)/4;

                      return ynext;
            }
    //метод рунге кутта ([1] формула 111)
           public static Double metod4p(Double xn, Double yn, Double hn){
                Double k1,k2,k3,k4,ynext;
                k1 = hn * f(xn, yn);
                k2 = hn * f(xn +  hn/3, yn + k1 / 3);
                k3 = hn * f(xn + 2 * hn/3, yn-k1/3+k2);
                k4=hn*f(xn+hn,yn+k1-k2+k3);
                ynext =yn+(k1 + 3 * k2 + 3* k3+k4) / 8;


                                return ynext;
            }


           public static void main(String[] args) throws IOException {
                Test(0.0,1.5,0.001,1.0,1.0,0.1);
           }
}
