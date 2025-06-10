package jrainroot.still_home.global.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jrainroot.still_home.entity.Member;
import jrainroot.still_home.global.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
  @Value("${custom.jwt.secretKey}")
  private String secretKeyOrigin;

  private SecretKey cashedSecretKey;

  public SecretKey getSecretKey() {
      if (cashedSecretKey == null) cashedSecretKey = _getSecretKey();
      return cashedSecretKey;
  }

  // 실제로 키값을 만드는 메서드
  private SecretKey _getSecretKey() {
      String KeyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyOrigin.getBytes());
      return Keys.hmacShaKeyFor(KeyBase64Encoded.getBytes());
  }

  public String genRefreshToken(Member member) {
      return genToken(member, 60 * 60 * 24 * 365 * 1); // 1 year
  }

  public String genAccessToken(Member member) {
      return genToken(member, 60 * 10); // 10 minutes
  }

  // 토큰 발행
  public String genToken(Member memeber, int seconds) {
      Map<String, Object> claims = new HashMap<>();

      claims.put("id", memeber.getId());
      claims.put("name", memeber.getName());
      // 권한이 필요한 경우 추가
    //   claims.put("authetication", member.getRole());

      long now = new Date().getTime();
      Date accessTokenExpiresIn = new Date(now + 1000L * seconds);

      return Jwts.builder()
              .claim("body", Util.json.toStr(claims))
              .setExpiration(accessTokenExpiresIn)
              .signWith(getSecretKey(), SignatureAlgorithm.HS512)
              .compact();
  }

  // 유효성 검사
  public boolean verify(String token) {
      try {
          Jwts.parserBuilder()
                  .setSigningKey(getSecretKey())
                  .build()
                  .parseClaimsJws(token);
      } catch (Exception e) {
          return false;
      }

      return true;
  }

  // 회원정보 가져오기
  public Map<String, Object> getClaims(String token) {
      String body = Jwts.parserBuilder()
              .setSigningKey(getSecretKey())
              .build()
              .parseClaimsJws(token)
              .getBody()
              .get("body", String.class);

      return Util.toMap(body);
  }
}
