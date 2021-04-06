package com.dke.data.agrirouter.impl.common.signing;

import com.dke.data.agrirouter.api.dto.revoke.RevokeRequest;
import com.dke.data.agrirouter.api.exception.CouldNotCreatePrivateKeyException;
import com.dke.data.agrirouter.impl.SignatureService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PrivateKeyCreationServiceTest {

  @Test
  void givenValidPrivateKeyCreatePrivateKeyShouldReturnValidResults() {
    String privateKey =
        "-----BEGIN PRIVATE KEY-----MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDYngadA9rWUDCoNQ/orXTId1W3QRrQ7ufLB8CDDY3g7w5hzBDhgPpavZlNrLaGqCzjDzYmSq5Km2KjAvAoGUwYeIhv7E1mK5LUL96PWujMag/FwHhUC3yXfjcyNLrM/zUPPL8dzITyRbdA7zANU2jYOrY13vlEpkP/AImoKDz9iRB6lJhT/JvdUo8TMH7JHNDW6LE4kuWV5VrUzu3fbpg20FjxK23fvA6lA0P+h+7xhL29/D54PfSnlK7jSu7c55L2EwxmtqMLn+ZEuV2bGsO5t7wmo3A9kB40atZJj/3+gFO2kuu/gXEgMQV2tRLU5pu0VhWz9HaJCGd23aGcZeYNAgMBAAECggEALLf24oxMraixWtBVf/HrJYVpRyMkH5TqekIX7pYqP5DqdasUoxEm3fu9PclVhxgJ6DgYNDB/qbxJUSDGpmiOipC7jeuJEPoW3kIhnpOkcqrEpcz1qlgSuQ2f5Hf5Rl+7OvGN/N9fZMqaqceXMnEROG5qdlWZPSBb8jVQtcvUx3wWaFsOOkHqZ4py0bTRLsZiUA3fmbdmNs2dE5CRutCkhZtnHRPZMvb2bxdxQlPm+NlpndCT63mThnry4ohC2bWNOqaaOkeQTwKYTzvu+rej5cGMni/6z/WxnhjID8eisB5ucPwKvWc2IOXBTjlKYB95jQkkQTeCmT/jLn4ZpmP3kQKBgQD6HD/cWB9Fl9ULbvtTBTtJxbFHZNO/j32Qlhw4fPd2b2eEleqMTb22xAeF20vq4xgVcsAr4tvG+boVAy+uZNs5mRVztEtP9WCdOryF/gEVfCG9J77RrkFXSA+puB40nY0IKfQ0ey4g9pY2ZMko9vNTsxKU/J9UONJjromemcETeQKBgQDdt95oCqiZcapjtVHZSLp2V3p1jozic7zfk16bGGFT73fNQt+oGXNH4SzH6j1Oad2GhiJMAnd5hnzsNpNSN80/JL1l0ac/ZcbOKk+Gkc1F4V9X52P7FAOs+epXbKg5iCKA3ztpN110W4WraaGAnf0WvggS74PpaT6bQwUiYe5ONQKBgQCKTbTCk7g3M46LlANGFU6DrN4rLIYrF53HpjpUwkXVKCWGG6BnlVWjCyNd0bZZXNqkgZ/ipMADd3R+yhRDy2GuqAhEwx4iSP3z89bsAVl4F/xQLPpH2KQpL5/5qeaKEdU6/ngbzUHk2HaX/YN/M4+6aPuNh2RTnlg45vsSyNMLgQKBgQDOgSK05LiDskpw88B4EDUbY/663zp48DNKSYgkDGEaPD/o/hAyEQI+QIvk51aSYifh06y9/s4ocgqIN4yfVznjReThoJOViMXR37aSL9pw66hRx0Jn6AxEX8DUDG/ipdj9Du3W4ao5ZcTqEkhJ8tx3Aiei+1JxY4TcN1/g1jWOwQKBgQCSqsUdmttSgb+9Jvp2ciyi+HwPEmuHGRyNSWHXZ6G4zwPDlDJ3s6Tbed5ChqBdFuB+jL9tezfxtJzvfaPmsouZi0h3epXWlG0FxB0D9CLQNO5IAvmJTZft4tF7AD1fXNIvPACqrV23rISFpIhmlQFBB/hpvp02eUuDwkdQcGSKag==-----END PRIVATE KEY-----";
    SecurityKeyCreationService privateKeyCreationService = new SecurityKeyCreationService();
    Assertions.assertNotNull(privateKeyCreationService.createPrivateKey(privateKey));
  }

  @Test
  void givenInvalidPrivateKeyCreatePrivateKeyShouldThrowException() {
    String invalidPrivateKey =
        "-----BEGIN PRIVATE KEY-----MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXxxxxxxxxoD54PfSnlK7jSu7c55L2EwxmtqMLn+ZEuV2bGsO5t7wmo3A9kB40atZJj/3+gFO2kuu/gXEgMQV2tRLU5pu0VhWz9HaJCGd23aGcZeYNAgMBAAECggEALLf24oxMraixWtBVf/HrJYVpRyMkH5TqekIX7pYqP5DqdasUoxEm3fu9PclVhxgJ6DgYNDB/qbxJUSDGpmiOipC7jeuJEPoW3kIhnpOkcqrEpcz1qlgSuQ2f5Hf5Rl+7OvGN/N9fZMqaqceXMnEROG5qdlWZPSBb8jVQtcvUx3wWaFsOOkHqZ4py0bTRLsZiUA3fmbdmNs2dE5CRutCkhZtnHRPZMvb2bxdxQlPm+NlpndCT63mThnry4ohC2bWNOqaaOkeQTwKYTzvu+rej5cGMni/6z/WxnhjID8eisB5ucPwKvWc2IOXBTjlKYB95jQkkQTeCmT/jLn4ZpmP3kQKBgQD6HD/cWB9Fl9ULbvtTBTtJxbFHZNO/j32Qlhw4fPd2b2eEleqMTb22xAeF20vq4xgVcsAr4tvG+boVAy+uZNs5mRVztEtP9WCdOryF/gEVfCG9J77RrkFXSA+puB40nY0IKfQ0ey4g9pY2ZMko9vNTsxKU/J9UONJjromemcETeQKBgQDdt95oCqiZcapjtVHZSLp2V3p1jozic7zfk16bGGFT73fNQt+oGXNH4SzH6j1Oad2GhiJMAnd5hnzsNpNSN80/JL1l0ac/ZcbOKk+Gkc1F4V9X52P7FAOs+epXbKg5iCKA3ztpN110W4WraaGAnf0WvggS74PpaT6bQwUiYe5ONQKBgQCKTbTCk7g3M46LlANGFU6DrN4rLIYrF53HpjpUwkXVKCWGG6BnlVWjCyNd0bZZXNqkgZ/ipMADd3R+yhRDy2GuqAhEwx4iSP3z89bsAVl4F/xQLPpH2KQpL5/5qeaKEdU6/ngbzUHk2HaX/YN/M4+6aPuNh2RTnlg45vsSyNMLgQKBgQDOgSK05LiDskpw88B4EDUbY/663zp48DNKSYgkDGEaPD/o/hAyEQI+QIvk51aSYifh06y9/s4ocgqIN4yfVznjReThoJOViMXR37aSL9pw66hRx0Jn6AxEX8DUDG/ipdj9Du3W4ao5ZcTqEkhJ8tx3Aiei+1JxY4TcN1/g1jWOwQKBgQCSqsUdmttSgb+9Jvp2ciyi+HwPEmuHGRyNSWHXZ6G4zwPDlDJ3s6Tbed5ChqBdFuB+jL9tezfxtJzvfaPmsouZi0h3epXWlG0FxB0D9CLQNO5IAvmJTZft4tF7AD1fXNIvPACqrV23rISFpIhmlQFBB/hpvp02eUuDwkdQcGSKag==-----END PRIVATE KEY-----";
    SecurityKeyCreationService privateKeyCreationService = new SecurityKeyCreationService();
    Assertions.assertThrows(
        CouldNotCreatePrivateKeyException.class,
        () -> privateKeyCreationService.createPrivateKey(invalidPrivateKey));
  }

  @Test
  void checkSignatureCanBeVerified() {
    String privateKey =
        "-----BEGIN PRIVATE KEY-----MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDMIkcJfcjTUm1wLwbXBcGoEn8vhOA6ew5fsJ+CNgo21BeNTgvOXzhZrwMZYuiAXFpmV4kukWjyLchFE5jKk6yPkmsPQXVnOWqYMQO6d/sutHBMtjQ55vOCwqDTR4rm2dHrs/Qg/PTBoH3Dcr6WKQNNUYMZNXzED2AC/CRkXjME0oMq7YzgkVxdwhftIrgmUUUCRgHngM2PPyNsmZckvXngkqWOr9eUCsbg2NzTet3shHE8eW5yEBXVeAvT3On1WoQvISOe1ULWFOZ6qZ1NDbwwEmZWwUr5Q33fGplaZCnohvPf+5IfEi+M4/pjnOVFC4oSSZCFF5Q7++bHKxsx18LXAgMBAAECggEADdLMaLIh5V2Rl6U3m1wCbzVBc6BV5t/qa0R0qMasDmZyadk9J25/TGNznEZ8ZCTc4k1PNt4V80BybWsUT7OMXfSWV2QhZSoYM9It5HS3h8QkTq2P/9PJ7bLsXSJdH6DkfEelYo5+rJYHrjKZAWYpmRTKAe0DU+uOnuUgOcO/RLY1mp5S15lJuCy2jwQml8g043/A4Hcjql5co2jT2Pxv+t9o6Y2wVbRUNGXgz63wGcny08AUmiGuH3fhB4RDHrOC/TKnJVovxF6nmcoxduC37VLywFRIt5Kgy4sweN9ezHrlRRnxekG6XcRfTzfhbXet0mE1i9nU5FlFZ4bm2Kpa8QKBgQD8dEXFKsZnUCaJNaLzFZpXihr+fmgIPAk558Qm+1dxDqKolfY204ZkuZduHrkSH2MWOD69+Ej1UoygQH6Dzn1MCMxEUQadpY/F8wCkq+30tMVvzsOH647tBSVLGdIpae6GaNOksyLKnzipCX7GCHIh9ej9iXGoph/G1bDPiVTRJQKBgQDPAEOOm5jP0g2dCbcNopupiPl/w4s1JPJdPtYsViUG5gvrYVQcdfOaS5dtO1jwnoThyGqpbRI3Zzs5xZgpTDTqlP1J5ys4e0DyW3x7gm0HZIXVsXa4y0qjL1S2p/LQZR3sBgarEurb5rRepJOHVeqNBmXipAE/j/d7JxsYk1R5SwKBgQC2jt5lViljTHKRhlfvsQ+LfjNHFeHlEoUZeqA+EEOLXioB/2+s9gmZjRwUZeMvV8Mvrjyw43re7HME756NciTpdvM/89f06GSvoKo2ap4I9zBPShXblFwmyjHNvT5c9F2olOV3lJL0M2+lzVf/nNvr8wgpT9sBOiCAiMbzTTwogQKBgGsFTOg/A9t2Uzl8m1p+VpJpUe0/UQqR5ohVA2/6vbv6VfFE6bKpPN/p8wkzZMFKg5MkBhlAXemtAo7U3N2FG9qoWufJj6vs9WAX8pz8ipgG3bbkwGe8GwORZe/llwEtNjIfz3TFHA3DEj/YQLn4roJo62Yoush9C0ttpXGoQkVPAoGBAN3EGJSmnb6vm3Vt6PyL8FfcBWM4m5YiUJxPIH+sVVNfNr3TSGqX3oOIoLTVShMq+guqtnTnLO8Cnmz9h8EkVMTNeHXJyY2WxHysnYaTbJu+pZSJQhuXs9ILFLFXsJnOZWFkrhTmytikNYFfBcBgybYoeGp12DaLiZkKOaKrxCMy-----END PRIVATE KEY-----";
    String publicKey =
        "-----BEGIN PUBLIC KEY-----\n"
            + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzCJHCX3I01JtcC8G1wXB\n"
            + "qBJ/L4TgOnsOX7CfgjYKNtQXjU4Lzl84Wa8DGWLogFxaZleJLpFo8i3IRROYypOs\n"
            + "j5JrD0F1ZzlqmDEDunf7LrRwTLY0OebzgsKg00eK5tnR67P0IPz0waB9w3K+likD\n"
            + "TVGDGTV8xA9gAvwkZF4zBNKDKu2M4JFcXcIX7SK4JlFFAkYB54DNjz8jbJmXJL15\n"
            + "4JKljq/XlArG4Njc03rd7IRxPHluchAV1XgL09zp9VqELyEjntVC1hTmeqmdTQ28\n"
            + "MBJmVsFK+UN93xqZWmQp6Ibz3/uSHxIvjOP6Y5zlRQuKEkmQhReUO/vmxysbMdfC\n"
            + "1wIDAQAB\n"
            + "-----END PUBLIC KEY-----";

    RevokeRequest revokeRequest = new RevokeRequest();
    revokeRequest.setAccountId("dflakjfafd");
    revokeRequest.setEndpointIds(new String[] {"adkfjlaf", "afdlakfdlakdfl"});
    revokeRequest.setTimeZone("UTC+5");
    revokeRequest.setUTCTimestamp("2019-05-18T23:03:21");

    Gson gson = new Gson();
    String input = gson.toJson(revokeRequest);

    class SignatureTester implements SignatureService {}

    SignatureTester signatureTester = new SignatureTester();

    byte[] signature = signatureTester.createSignature(input, privateKey);

    signatureTester.verifySignature(input, signature, publicKey);
  }
}
