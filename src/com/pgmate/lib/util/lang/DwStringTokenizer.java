package com.pgmate.lib.util.lang;

public class DwStringTokenizer {
	
	
//	private	String	org_data	=	null;
	private	String	data		=	null;
	private	String	deli		=	null;
	private String  now_str     =    null;

	public DwStringTokenizer(String data, String deli) {
//		this.org_data	=	data;
		this.data		=	data.trim();
		this.deli		=	deli;
	}

	public int countTokens() {
		if(data == null || deli == null) return 0;

		int idx		=	0;
		int	count	=	0;

		boolean	stop	=	false;
		while(!stop) {
			idx		=	data.indexOf(deli, idx);
			count++;

			if(idx < 0) break;
			idx		+=	deli.length();
		}

		return count;
	}

	public boolean hasMoreTokens() {
		if(data == null || deli == null) return false;
		else return true;
	}

	public String nextToken() {
		if(data == null || deli == null) return null;

		String	buf	=	null;
		int idx		=	data.indexOf(deli);

		if(idx < 0) {
			buf		=	data;
			data	=	null;

			return buf;
		}

		buf		=	data.substring(0, idx);
		data	=	data.substring(idx + deli.length());
		now_str = data.trim();
		return buf.trim();
	}

	public String String()
	{
		return now_str;
	}
	
	public String getString()
	{
		return nextToken();
	}
	
	public int getInt()
	{
		return Integer.parseInt(nextToken());
	}	
	
    public String nextData() {
            if(data == null) return null;
            return data;
    }

}

