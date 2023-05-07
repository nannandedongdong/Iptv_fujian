
package pullscreen;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Packet {
	
	private byte[] sig = new byte[] {'C', 'C', 'D', 'T' };
	private byte[] payload = new byte[0];

	private short packettype; 
	
	private InetAddress hostAdr;
	private int hostPort;

	//包最大20*1024+12
//	final short MAX_PACKET_SIZE  = 20492;
	final short MAX_PACKET_SIZE  = 1024;
	final short HEADER_SIZE      = 12;
	final short MAX_PAYLOAD_SIZE = MAX_PACKET_SIZE - HEADER_SIZE;
    
    //支持的包类型
	final byte PT_HELO          = 0x01;
	final byte PT_BYE           = 0x02;
	final byte PT_KEY           = 0x03;
	final byte PT_TOUCH         = 0x04;
    final byte PT_SENSOR        = 0x05;
    final byte PT_MIRROR        = 0x06;
	final byte PT_PING          = 0x07;
	final byte PT_NOTIFICATION  = 0x08;

	final byte BT_USE_NAME   = 0x01;
	final byte BT_DOWN       = 0x02;
	final byte BT_UP         = 0x04;
	final byte BT_NO_REPEAT  = 0x20;

	final byte TOUCH_DOWN    = 0x01;
	final byte TOUCH_UP      = 0x02;
	final byte TOUCH_DOWN_MOVE = 0x05;//组合Flag

	public Packet(InetAddress Adr, int Port)
	{
		if(Adr==null||Port==0){
			return;
		}
		hostAdr = Adr;
		hostPort =  Port;
	}

	protected void appendPayload(String payload)
	{
		byte[] payloadarr = payload.getBytes();
		int oldpayloadsize = this.payload.length;
		byte[] oldpayload = this.payload;
        // +1为了存储 '\0'
		this.payload = new byte[oldpayloadsize+payloadarr.length+1];
		System.arraycopy(oldpayload, 0, this.payload, 0, oldpayloadsize);
		System.arraycopy(payloadarr, 0, this.payload, oldpayloadsize, payloadarr.length);
	}

	/* 追加单字节 */
	protected void appendPayload(byte payload)
	{
		appendPayload(new byte[] { payload });
	}

	/* 追加字节数组 */
	protected void appendPayload(byte[] payloadarr)
	{
		int oldpayloadsize = this.payload.length;
		byte[] oldpayload = this.payload;
		this.payload = new byte[oldpayloadsize+payloadarr.length];
		System.arraycopy(oldpayload, 0, this.payload, 0, oldpayloadsize);
		System.arraycopy(payloadarr, 0, this.payload, oldpayloadsize, payloadarr.length);
	}

	/* 追加int */
	protected void appendPayload(int i) {
		appendPayload(intToByteArray(i));
	}

	/* 追加short */
	protected void appendPayload(short s) {
		appendPayload(shortToByteArray(s));
	}
	
	/* 获得要发送的包数量 */
	public int getNumPackets()
	{
		return (int)((payload.length + (MAX_PAYLOAD_SIZE - 1)) / MAX_PAYLOAD_SIZE);
	}
	
	/* 获得包头信息(12 Bytes) */
	private byte[] getHeader(int seq, int maxseq, short actpayloadsize)
	{
		byte[] header = new byte[HEADER_SIZE];
        /* H1 Signature */
		System.arraycopy(sig, 0, header, 0, 4);
		byte[] PacketType = shortToByteArray(this.packettype);
        /* H2 PacketType */
		System.arraycopy(PacketType, 0, header, 4, 2);
		byte[] seqarr = intToByteArray(seq);
        /* H3 Sequence number */
		System.arraycopy(seqarr, 0, header, 6, 2);
		byte[] maxseqarr = intToByteArray(maxseq);
        /* H4 Number of packets */
		System.arraycopy(maxseqarr, 0, header, 8, 2);
		byte[] payloadsize = shortToByteArray(actpayloadsize);
        /* H5 Payload size */
		System.arraycopy(payloadsize, 0, header, 10, 2);	
		return header;
	}
	
	/* 生成包含Head和Payload的UDP包 */
	private byte[] getUDPMessage(int seq)
	{
		int maxseq = (int)((payload.length + (MAX_PAYLOAD_SIZE - 1)) / MAX_PAYLOAD_SIZE);
		if(seq > maxseq)
			return null;
		
		short actpayloadsize;
		
		if(seq == maxseq)
			actpayloadsize = (short)(payload.length%MAX_PAYLOAD_SIZE);
		else
			actpayloadsize = (short)MAX_PAYLOAD_SIZE;

		byte[] pack = new byte[HEADER_SIZE+actpayloadsize];
		
		System.arraycopy(getHeader(seq, maxseq, actpayloadsize), 0, pack, 0, HEADER_SIZE);
		System.arraycopy(payload, (seq-1)*MAX_PAYLOAD_SIZE, pack, HEADER_SIZE, actpayloadsize);
		
		return pack;
	}
	
	/* 发包 */
	public void send() throws IOException
	{
		int maxseq = getNumPackets();
		DatagramSocket s = new DatagramSocket();
		
		// For each Packet in Sequence...
		for(int seq=1;seq<=maxseq;seq++)
		{
			// Get Message and send them...
			byte[] pack = getUDPMessage(seq);
			DatagramPacket p = new DatagramPacket(pack, pack.length);
			p.setAddress(hostAdr);
			p.setPort(hostPort);
			s.send(p);
		}
		s.close();
	}
	
	/*convert an integer to a Byte array*/
	private static final byte[] intToByteArray(int value) {
	          return new byte[] {
	                  (byte)(value >>> 24),
	                  (byte)(value >>> 16),
	                  (byte)(value >>> 8),
	                  (byte)value};
	}

	/*convert an short to a Byte array*/
	private static final byte[] shortToByteArray(short value) {
        return new byte[] { (byte)(value >>> 8), (byte)value};
	}

    /* HELO数据包  flags当前固定为0x01 */
	public void PacketHELO(byte flags)
	{
		packettype = PT_HELO;
		appendPayload(flags);
	}

	/* PING数据包 */
	public void PacketPING(byte flags)
	{
		packettype = PT_PING;
		appendPayload(flags);
	}
	
	/* BYE数据包 */
	public void PacketBYE(byte flags)
	{
		packettype = PT_BYE;
		appendPayload(flags);
	}

    /* 按键数据包 */
	public void PacketKEY(byte code, String btn_name, boolean repeat, boolean down)
	{
		packettype = PT_KEY;
		byte flags = 0;
		
        if(down)
            flags |= BT_DOWN;
        else
            flags |= BT_UP;
        
        if(!repeat)
            flags |= BT_NO_REPEAT;

		if(btn_name != null)
			flags |= BT_USE_NAME;

		appendPayload(code);
		appendPayload(flags);
		appendPayload(btn_name);
	}

	public void PacketTOUCH(boolean down,short x, short y)
	{
		packettype = PT_TOUCH;
		byte flags = 0;
		if(down)
			flags |= TOUCH_DOWN_MOVE;
		else
			flags |= TOUCH_UP;
		System.out.println("碼值："+flags);
		appendPayloadTOUCH(x, y, flags);
	}

	private void appendPayloadTOUCH(short x, short y, byte flags) {
		appendPayload(flags);
		appendPayload( x );
		appendPayload( y );
	}
	public void PacketTOUCH_LongClick(boolean down,short x, short y)
	{
		packettype = PT_TOUCH;
		byte flags = 4;
		System.out.println("碼值："+flags);
		appendPayloadTOUCH(x, y, flags);
	}


	public void PacketNOTIFICATION(String message)
	{
		packettype = PT_NOTIFICATION;
		appendPayload(message);
	}
	
}

