package pullscreen;
/**
 * @Package com.ccdt.pullscreen
 * @ClassName: Constants
 * @Description: 拉屏的常量
 * @author hezb
 * @date 2015年6月24日 上午10:23:55
 */

public interface Constants {
    
    /** 手机收包 端口 */
    public static final int PHONE_PORT = 9888;
    /** 盒子收包 端口 */
    public static final int STB_PORT = 9877;
    /** 手机发送的拉屏命令 */
    public static final String PULL_SCREEN = "pull screen";
    /** 自定义用于分割的字符串 */
    public static final String DIVIDE = " , ";
    /** 手机传参 key */
    public static final String URL = "url";//播放地址
    public static final String POSITION = "position";//盒子播放的当前位置
    public static final String NAME = "name";//影片名
    
}
