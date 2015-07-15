package gpps.token.service;

public interface ITokenService {
	public boolean isValid(String token);
	public void login(String token, String loginId) throws Exception;
	public String createToken(Integer lenderId);
}
