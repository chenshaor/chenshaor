package xrc.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

public class User implements Serializable {

    private Integer id;
    private String name;
    private String pwd;
    private String salt;


    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", salt=" + salt +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    //秘钥生成方法
    //输入为陷门值t和椭圆曲线群ecgroup
    //输出为2*(t+1)大小的数组fx,代表该用户生成的两个t次多项式的2*(t+1)个系数
    public static BigInteger[][] keygen(int t, ECGroup ecgroup){
        BigInteger secret[][] = new BigInteger[2][t+1];//选择t+1个a值和b值
        for(int i=0; i<=t; i++) {
            BigInteger a = new BigInteger(ecgroup.n.bitLength(), new Random());
            BigInteger b = new BigInteger(ecgroup.n.bitLength(), new Random());
            secret[0][i] = a;
            secret[1][i] = b;

        }
        return secret;
    }

    //计算C
    //输入为陷门值t、用户的2*(t+1)个多项式系数fx、椭圆曲线群ecgroup、椭圆曲线点h
    //输出为该用户的t+1个C值
    public static ECPoint[] C(int t,BigInteger[][] fx,ECGroup ecgroup,ECPoint h){
        ECPoint g = new ECPoint(ecgroup.x1,ecgroup.y1);
        ECPoint C[] = new ECPoint[t+1];
        for(int i=0; i<=t; i++){
            System.out.println(i);
            C[i] = ecgroup.add(ecgroup.multiply(g, fx[0][i]),ecgroup.multiply(h, fx[1][i]));
        }


        return C;
    }

    //计算A
    //输入为陷门值t、用户的2*(t+1)个多项式系数fx、椭圆曲线群ecgroup
    //输出为该用户的t+1个A值
    public static ECPoint[] A(int t,BigInteger[][] fx,ECGroup ecgroup){
        ECPoint g = new ECPoint(ecgroup.x1,ecgroup.y1);
        ECPoint A[] = new ECPoint[t+1];
        for(int i=0; i<=t; i++)
            A[i] = ecgroup.multiply(g, fx[0][i]);
        return A;
    }
}
