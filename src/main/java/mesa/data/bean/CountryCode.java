package mesa.data.bean;

public class CountryCode {
	private String name;
	private String code;

	public CountryCode(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public boolean match(String text) {
		String match = name.toLowerCase();
		String matchAgainst = text.toLowerCase();
		int index = -1;
		for(char c : matchAgainst.toCharArray()) {
			int pos = match.indexOf(c, index + 1);
			if(pos == -1) {
				return false;
			}else {
				index = pos;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "CountryCode [name=" + name + ", code=" + code + "]";
	}
}
