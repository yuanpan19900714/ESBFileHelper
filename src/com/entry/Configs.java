package com.entry;

public class Configs {
	private final static String IN_CONF = "F:/SVNCheckout/湖北银行ESB/工程管理/03版本/02集成测试环境/SIT提版/20181009/SmartESB_yuanpan_20181009n/增量版本/SmartESB/configs/in_conf";
	private final static String OUT_CONF = "F:/SVNCheckout/湖北银行ESB/工程管理/03版本/02集成测试环境/SIT提版/20181009/SmartESB_yuanpan_20181009n/增量版本/SmartESB/configs/out_conf";
//	private final static String IN_CONF = "F:/SVNCheckout/湖北银行ESB/工程管理/09code/SmartESB/configs/in_conf";
//	private final static String OUT_CONF = "F:/SVNCheckout/湖北银行ESB/工程管理/09code/SmartESB/configs/out_conf";
	
	public class inConf {
		public static final String METADATA = IN_CONF + "/metadata";
		public static final String SERVICE_FIELD = IN_CONF + "/serviceField";
//		public static final String SERVICE_INDENTIFY = IN_CONF + "/serviceIdentify_add.xml";
		public static final String SERVICE_INDENTIFY = IN_CONF + "/serviceIdentify.xml";
	}

	public class outConf {
		public static final String METADATA = OUT_CONF + "/metadata";
		public static final String SERVICE_FIELD = OUT_CONF + "/serviceField";
//		public static final String SYSTEM_INDENTIFY = OUT_CONF + "/systemIdentify_add.xml";
		public static final String SYSTEM_INDENTIFY = OUT_CONF + "/systemIdentify.xml";
	}
	
	//报文格式
	public class msgMode{
		public static final String STN = "fix";			//标准：定长+xml
		public static final String STN_XML = "clxml";	//标准xml：长亮xml
		public static final String TRA_XML = "xml";		//传统xml：常见xml
	}
	
	public class msgExp{
		public static final String STN_OFFSET = "offset";
		public static final String STN_LENGTH = "length";
		public static final String STN_XML_SVCCOD = "/root/sys/svccod";
		public static final String STN_XML_BSICOD = "/root/sys/bsicod";
		public static final String TRA_XML_DH = "/Document/Head/";
		public static final String TRA_XML_RH = "/request/head/";
		public static final String TRA_XML_IH = "/ibs/head/";
		public static final String TRA_XML_MH = "/message/head/";
	}
}
