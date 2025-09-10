package kr.letech.assign.http;

public class MimeTypes {
	public static String of(String name){
		 String ext = name.replaceAll("^.*\\.", "").toLowerCase();
		 
		 switch (ext) {
		   case "html":
		   case "htm":
			   return "text/html; charset=utf-8";
		   case "css":
			   return "text/css; charset=utf-8";
		   case "js":
			   return "text/javascript; charset=utf-8";
		   case "svg":
			   return "image/svg+xml";
		   case "png":
		   	   return "image/png";
		   case "jpg":
		   case "jpeg":
			   return "image/jpeg";
		   case "gif":
			   return "image/gif";
		   default:
			   return"application/octet-stream";
		 }
	}
	
	public static enum Value {
		OCTET_STREAM("octet-stream", "application/octet-Stream");

		private final String call;
		private final String mimeType;
		
		Value(String call, String mimeType) {
			this.call = call;
			this.mimeType = mimeType;
		}
		
		public static String getMimeType(String call) {
			for(Value mt : values()) {
				if(mt.call.equalsIgnoreCase(call)) 
				return mt.mimeType;
			}
			return null;
		}
	}
}
