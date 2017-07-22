package com.bsi.presensidosen.helper;

/**
 * Created by Mukhlasin on 9/14/2016.
 */
public class AppKey {
    private final String TAG = AppKey.class.getSimpleName();
    private static final String ip = "http://10.0.1.106/presensidosen";
    private static final String ipcv = "http://10.0.1.106/cek_versi_aplikasi/index.php";
    private static final String BASE_URL = "http://10.0.1.106/presensidosen/index.php";

    public static  String getURLImageUmy(String photos){
        return ip + "/fotopegawai/" + photos;
    }

    public static  String getURLImageUmyEmpty(){
        return ip + "/fotopegawai/null.jpg";
    }

    public static String getUserLogin(String email, String password) {
        return BASE_URL + "?route=login&email=" + email + "&password=" + password;
    }

    public static String getAddCheckIn(int id_pegawai, long tanggalku, long jamdatang, String ipmasuk) {
        return BASE_URL + "?route=addcheckin&id_pegawai=" + id_pegawai + "&tanggal=" + tanggalku + "&jam_datang=" + jamdatang + "&ip_masuk=" + ipmasuk ;
    }

    public static String getAddCheckOut(int id_pegawai, long tanggalku, long jam_pulang, String ip_keluar) {
        return BASE_URL + "?route=addcheckout&id_pegawai=" + id_pegawai + "&tanggal=" +tanggalku + "&jam_pulang=" + jam_pulang + "&ip_keluar=" + ip_keluar;
    }

    public static String getDetailPrecense(int idpegawai, String sbulan, String stahun) {
        return BASE_URL + "?route=getdetailprecense&id_pegawai=" + idpegawai + "&bulan=" + sbulan + "&tahun=" + stahun;
    }

    public static String getAppVersion(String appversion) {
        return ipcv + "?route=appversion&app_version=" + appversion ;
    }

    public static String getIpPublic() {
        return "http://api.ipify.org/?format=json";
    }

    public static String getTimeServer() {
        return BASE_URL + "?route=gettimeserver&jam";
    }

    public static String getBulanSpinner() {
        return BASE_URL + "?route=getbulanspinner&id_bulan";
    }
    public static String getTahunSpinner() {
        return BASE_URL + "?route=gettahunspinner&id_tahun";
    }

    public static String getLastPrecense(int idpegawai) {
        return BASE_URL + "?route=getlastprecense&id_pegawai=" + idpegawai;
    }

    public static final String KEY_STATUS = "status";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_DATA = "data";
    public static final String KEY_DATADETAIL = "datadetail";
    public static final String KEY_JAMSERVER = "jamserver";
    public static final String KEY_SPINNER_BULAN = "spinnerbulan";
    public static final String KEY_SPINNER_TAHUN = "spinnertahun";
    public static final String KEY_LAST_PRECENSE = "lastprecense";
    public static final String KEY_DATA_VERSI = "cekversi";

}
