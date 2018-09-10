package com.dke.data.agrirouter.impl.onboard.secured;

import com.dke.data.agrirouter.api.env.QA;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

class OnboardingServiceImplTest {

    @Test
    void rq31_givenValidPrivateKeyAndMatchingPublicKey_VerifySignature_ShouldReturnBeSuccessful() {
        String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCcun3/je3pK4I5\n" +
                "sLzHHPTkIVyPYJMLa308ISOBq/5SOaA0VQgz1/ezEqjy6FN5ThJJfKtRBGF/WxYV\n" +
                "pmLp7IiR/K3VUvr1tUQ/b/amc1rDvapO0SNgsHiZVFCXdFTqyn3GoKx6kiejr0av\n" +
                "dL3zTBuH4hGST72ukCAQ3XKcQl16KreErt/HsYqo4BD3bsUmP4cD4FWAy43YkGsK\n" +
                "aTm+9Kgo543L2umNrAgsFKJCdP+WFeZeKzJFLAsfyriwnEqdcs/gt0NvNN+YtSBZ\n" +
                "ffKo+DcoziOFtCMUL3uPK4QU2jue315lofpg5aa1lhB6wLBMsZF4gNO/gGHwb1KA\n" +
                "Y4CJGwu9AgMBAAECggEAJ3NYAOpIbw9pHb1rn1EeIYh2RI7iJJc/dFHBHsRc5XSk\n" +
                "61dWE7qymmxBg7dQP7ITr1TGyMNqCUUzx2pU7p3/+INafF6BVjT1A2Ta+KYDvE6z\n" +
                "HljNZ2q0SKAtExqQv3MN+t/52naMrMqTZ9nicGoBahBRLPuOevDDaYtcbJHV9siB\n" +
                "/cuPWgQwHhosCFv7qL5BtCUHoZ2H2eFJ5cGM0QV4slU7+K5tFR7mf9soPfOdiXli\n" +
                "okSW0cX/rVXEXQIplm8DYQz09Kb5txSkMTcGFLX2Rn24pzUVlLERZD3cRIHLJO88\n" +
                "Y+/TG2ND6oHRWBLhvK43xxnWXy6M7VVLvGuXLZwxbwKBgQDNwTCZMZ/SyFtNksTM\n" +
                "zUq3ATG3+47hZ2KoCyOjpqWDpcWNA/DF87H6aV7ARpAh6E+ku70AXNReDv0DOn/t\n" +
                "PVfjfWNkXNVeRz8r9E+A323gQniMzG96smALaCKV9eryQog3PmklrKfX/DWmSV8w\n" +
                "NQo8Y/ckC8kVcC6k81mBRJkpywKBgQDDAGr57IWXp/72DX0lEXDcK7OTse13zg/u\n" +
                "Uu68eJDKPpbEUIpi3JcHWPUmYTtNyFvWRIxUle9CzkDqKGDmhKZIK7w/kk7lC+6+\n" +
                "+aiV5x4tFgXGcauQVvfmsWyox1r9buUEe3neNLCH2OAcUrIJSEAWutzbnHR/fRdX\n" +
                "7q0QO+GPlwKBgAm6t+yWjyMtfDvH0+yHTKAou0wi5he8hsrBdBb5+1ulTBkl9Y22\n" +
                "v1TDBMr8yzX/QTzd78sDmLs15Drx2jjCrNRCCqAlT6Wcot0WOWgzqINxQTgPNrpL\n" +
                "kwUXEIOc1FZOVRifp+MzLeCluXe6gnjKGUjPxVD8Ca8s1dfQ9guJzeBPAoGAZoTW\n" +
                "uRR/W68H3SZUT+OueSJEo/acSSs9tudE+jNMs/dZ3lYVm0or4MIFCKt/uTIIDks1\n" +
                "l9bH1lCHUlPuNeMxZKoRqz5pWgP+/cpVtpGujLeSS7VBJH6EkHVhEg7VKH5fyw57\n" +
                "kLYUxSbhFCLp1PV2ND0DljXYDZqfZixmMi25NXsCgYAO3ZOrObDLJpEDCFo6ArEr\n" +
                "YCBM03ETtFMnd+zDJxY2REhYh+HWgHZ0UvGPB9kYslT9MwvJNNKPnmcQP5n5J5RF\n" +
                "YE6rVQRynPGy7+gAqB0omgknPKHzKMKZChWDiY0gN85s6wHNsf7u5qgnj5b/k+Zq\n" +
                "+0h0jlv7SvLRdCo5vjbVkw==\n" +
                "-----END PRIVATE KEY-----\n";
        String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnLp9/43t6SuCObC8xxz0\n" +
                "5CFcj2CTC2t9PCEjgav+UjmgNFUIM9f3sxKo8uhTeU4SSXyrUQRhf1sWFaZi6eyI\n" +
                "kfyt1VL69bVEP2/2pnNaw72qTtEjYLB4mVRQl3RU6sp9xqCsepIno69Gr3S980wb\n" +
                "h+IRkk+9rpAgEN1ynEJdeiq3hK7fx7GKqOAQ927FJj+HA+BVgMuN2JBrCmk5vvSo\n" +
                "KOeNy9rpjawILBSiQnT/lhXmXisyRSwLH8q4sJxKnXLP4LdDbzTfmLUgWX3yqPg3\n" +
                "KM4jhbQjFC97jyuEFNo7nt9eZaH6YOWmtZYQesCwTLGReIDTv4Bh8G9SgGOAiRsL\n" +
                "vQIDAQAB\n" +
                "-----END PUBLIC KEY-----\n";
        OnboardingServiceImpl onboardingService = new OnboardingServiceImpl(new QA());
        String content = "SOME CONTENT TO SIGN";
        byte[] signature = onboardingService.createSignature(content, privateKey);
        onboardingService.verifySignature(content, signature, publicKey);
    }

    @Test
    void rq31_givenValidPrivateKeyAndMatchingPublicKey_VerifySignature_ShouldReturnBeSuccessfulForRealContent() {
        String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCcun3/je3pK4I5\n" +
                "sLzHHPTkIVyPYJMLa308ISOBq/5SOaA0VQgz1/ezEqjy6FN5ThJJfKtRBGF/WxYV\n" +
                "pmLp7IiR/K3VUvr1tUQ/b/amc1rDvapO0SNgsHiZVFCXdFTqyn3GoKx6kiejr0av\n" +
                "dL3zTBuH4hGST72ukCAQ3XKcQl16KreErt/HsYqo4BD3bsUmP4cD4FWAy43YkGsK\n" +
                "aTm+9Kgo543L2umNrAgsFKJCdP+WFeZeKzJFLAsfyriwnEqdcs/gt0NvNN+YtSBZ\n" +
                "ffKo+DcoziOFtCMUL3uPK4QU2jue315lofpg5aa1lhB6wLBMsZF4gNO/gGHwb1KA\n" +
                "Y4CJGwu9AgMBAAECggEAJ3NYAOpIbw9pHb1rn1EeIYh2RI7iJJc/dFHBHsRc5XSk\n" +
                "61dWE7qymmxBg7dQP7ITr1TGyMNqCUUzx2pU7p3/+INafF6BVjT1A2Ta+KYDvE6z\n" +
                "HljNZ2q0SKAtExqQv3MN+t/52naMrMqTZ9nicGoBahBRLPuOevDDaYtcbJHV9siB\n" +
                "/cuPWgQwHhosCFv7qL5BtCUHoZ2H2eFJ5cGM0QV4slU7+K5tFR7mf9soPfOdiXli\n" +
                "okSW0cX/rVXEXQIplm8DYQz09Kb5txSkMTcGFLX2Rn24pzUVlLERZD3cRIHLJO88\n" +
                "Y+/TG2ND6oHRWBLhvK43xxnWXy6M7VVLvGuXLZwxbwKBgQDNwTCZMZ/SyFtNksTM\n" +
                "zUq3ATG3+47hZ2KoCyOjpqWDpcWNA/DF87H6aV7ARpAh6E+ku70AXNReDv0DOn/t\n" +
                "PVfjfWNkXNVeRz8r9E+A323gQniMzG96smALaCKV9eryQog3PmklrKfX/DWmSV8w\n" +
                "NQo8Y/ckC8kVcC6k81mBRJkpywKBgQDDAGr57IWXp/72DX0lEXDcK7OTse13zg/u\n" +
                "Uu68eJDKPpbEUIpi3JcHWPUmYTtNyFvWRIxUle9CzkDqKGDmhKZIK7w/kk7lC+6+\n" +
                "+aiV5x4tFgXGcauQVvfmsWyox1r9buUEe3neNLCH2OAcUrIJSEAWutzbnHR/fRdX\n" +
                "7q0QO+GPlwKBgAm6t+yWjyMtfDvH0+yHTKAou0wi5he8hsrBdBb5+1ulTBkl9Y22\n" +
                "v1TDBMr8yzX/QTzd78sDmLs15Drx2jjCrNRCCqAlT6Wcot0WOWgzqINxQTgPNrpL\n" +
                "kwUXEIOc1FZOVRifp+MzLeCluXe6gnjKGUjPxVD8Ca8s1dfQ9guJzeBPAoGAZoTW\n" +
                "uRR/W68H3SZUT+OueSJEo/acSSs9tudE+jNMs/dZ3lYVm0or4MIFCKt/uTIIDks1\n" +
                "l9bH1lCHUlPuNeMxZKoRqz5pWgP+/cpVtpGujLeSS7VBJH6EkHVhEg7VKH5fyw57\n" +
                "kLYUxSbhFCLp1PV2ND0DljXYDZqfZixmMi25NXsCgYAO3ZOrObDLJpEDCFo6ArEr\n" +
                "YCBM03ETtFMnd+zDJxY2REhYh+HWgHZ0UvGPB9kYslT9MwvJNNKPnmcQP5n5J5RF\n" +
                "YE6rVQRynPGy7+gAqB0omgknPKHzKMKZChWDiY0gN85s6wHNsf7u5qgnj5b/k+Zq\n" +
                "+0h0jlv7SvLRdCo5vjbVkw==\n" +
                "-----END PRIVATE KEY-----\n";
        String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnLp9/43t6SuCObC8xxz0\n" +
                "5CFcj2CTC2t9PCEjgav+UjmgNFUIM9f3sxKo8uhTeU4SSXyrUQRhf1sWFaZi6eyI\n" +
                "kfyt1VL69bVEP2/2pnNaw72qTtEjYLB4mVRQl3RU6sp9xqCsepIno69Gr3S980wb\n" +
                "h+IRkk+9rpAgEN1ynEJdeiq3hK7fx7GKqOAQ927FJj+HA+BVgMuN2JBrCmk5vvSo\n" +
                "KOeNy9rpjawILBSiQnT/lhXmXisyRSwLH8q4sJxKnXLP4LdDbzTfmLUgWX3yqPg3\n" +
                "KM4jhbQjFC97jyuEFNo7nt9eZaH6YOWmtZYQesCwTLGReIDTv4Bh8G9SgGOAiRsL\n" +
                "vQIDAQAB\n" +
                "-----END PUBLIC KEY-----\n";
        String requestBody = "{\"id\":\"com.dke.data.agrirouter.testmanagement.integration.requirements.onboard.SecuredOnboardingTest\",\"applicationId\":\"12e450c0-cab9-4ad0-8537-841a3b6ee1aa\",\"certificationVersionId\":\"cc5d513f-45e9-455c-be81-bb697d7e071b\",\"gatewayId\":\"3\",\"certificateType\":\"PEM\",\"UTCTimestamp\":\"2018-07-03T15:38:21.240122Z\",\"timeZone\":\"+02:00\"}";
        OnboardingServiceImpl onboardingService = new OnboardingServiceImpl(new QA());
        byte[] signature = onboardingService.createSignature(requestBody, privateKey);
        onboardingService.verifySignature(requestBody, signature, publicKey);
    }

    @Test
    void rq31_givenValidPrivateKeyAndMatchingPublicKey_VerifySignatureAfterHex_ShouldReturnBeSuccessful() throws Throwable {
        String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCcun3/je3pK4I5\n" +
                "sLzHHPTkIVyPYJMLa308ISOBq/5SOaA0VQgz1/ezEqjy6FN5ThJJfKtRBGF/WxYV\n" +
                "pmLp7IiR/K3VUvr1tUQ/b/amc1rDvapO0SNgsHiZVFCXdFTqyn3GoKx6kiejr0av\n" +
                "dL3zTBuH4hGST72ukCAQ3XKcQl16KreErt/HsYqo4BD3bsUmP4cD4FWAy43YkGsK\n" +
                "aTm+9Kgo543L2umNrAgsFKJCdP+WFeZeKzJFLAsfyriwnEqdcs/gt0NvNN+YtSBZ\n" +
                "ffKo+DcoziOFtCMUL3uPK4QU2jue315lofpg5aa1lhB6wLBMsZF4gNO/gGHwb1KA\n" +
                "Y4CJGwu9AgMBAAECggEAJ3NYAOpIbw9pHb1rn1EeIYh2RI7iJJc/dFHBHsRc5XSk\n" +
                "61dWE7qymmxBg7dQP7ITr1TGyMNqCUUzx2pU7p3/+INafF6BVjT1A2Ta+KYDvE6z\n" +
                "HljNZ2q0SKAtExqQv3MN+t/52naMrMqTZ9nicGoBahBRLPuOevDDaYtcbJHV9siB\n" +
                "/cuPWgQwHhosCFv7qL5BtCUHoZ2H2eFJ5cGM0QV4slU7+K5tFR7mf9soPfOdiXli\n" +
                "okSW0cX/rVXEXQIplm8DYQz09Kb5txSkMTcGFLX2Rn24pzUVlLERZD3cRIHLJO88\n" +
                "Y+/TG2ND6oHRWBLhvK43xxnWXy6M7VVLvGuXLZwxbwKBgQDNwTCZMZ/SyFtNksTM\n" +
                "zUq3ATG3+47hZ2KoCyOjpqWDpcWNA/DF87H6aV7ARpAh6E+ku70AXNReDv0DOn/t\n" +
                "PVfjfWNkXNVeRz8r9E+A323gQniMzG96smALaCKV9eryQog3PmklrKfX/DWmSV8w\n" +
                "NQo8Y/ckC8kVcC6k81mBRJkpywKBgQDDAGr57IWXp/72DX0lEXDcK7OTse13zg/u\n" +
                "Uu68eJDKPpbEUIpi3JcHWPUmYTtNyFvWRIxUle9CzkDqKGDmhKZIK7w/kk7lC+6+\n" +
                "+aiV5x4tFgXGcauQVvfmsWyox1r9buUEe3neNLCH2OAcUrIJSEAWutzbnHR/fRdX\n" +
                "7q0QO+GPlwKBgAm6t+yWjyMtfDvH0+yHTKAou0wi5he8hsrBdBb5+1ulTBkl9Y22\n" +
                "v1TDBMr8yzX/QTzd78sDmLs15Drx2jjCrNRCCqAlT6Wcot0WOWgzqINxQTgPNrpL\n" +
                "kwUXEIOc1FZOVRifp+MzLeCluXe6gnjKGUjPxVD8Ca8s1dfQ9guJzeBPAoGAZoTW\n" +
                "uRR/W68H3SZUT+OueSJEo/acSSs9tudE+jNMs/dZ3lYVm0or4MIFCKt/uTIIDks1\n" +
                "l9bH1lCHUlPuNeMxZKoRqz5pWgP+/cpVtpGujLeSS7VBJH6EkHVhEg7VKH5fyw57\n" +
                "kLYUxSbhFCLp1PV2ND0DljXYDZqfZixmMi25NXsCgYAO3ZOrObDLJpEDCFo6ArEr\n" +
                "YCBM03ETtFMnd+zDJxY2REhYh+HWgHZ0UvGPB9kYslT9MwvJNNKPnmcQP5n5J5RF\n" +
                "YE6rVQRynPGy7+gAqB0omgknPKHzKMKZChWDiY0gN85s6wHNsf7u5qgnj5b/k+Zq\n" +
                "+0h0jlv7SvLRdCo5vjbVkw==\n" +
                "-----END PRIVATE KEY-----\n";
        String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnLp9/43t6SuCObC8xxz0\n" +
                "5CFcj2CTC2t9PCEjgav+UjmgNFUIM9f3sxKo8uhTeU4SSXyrUQRhf1sWFaZi6eyI\n" +
                "kfyt1VL69bVEP2/2pnNaw72qTtEjYLB4mVRQl3RU6sp9xqCsepIno69Gr3S980wb\n" +
                "h+IRkk+9rpAgEN1ynEJdeiq3hK7fx7GKqOAQ927FJj+HA+BVgMuN2JBrCmk5vvSo\n" +
                "KOeNy9rpjawILBSiQnT/lhXmXisyRSwLH8q4sJxKnXLP4LdDbzTfmLUgWX3yqPg3\n" +
                "KM4jhbQjFC97jyuEFNo7nt9eZaH6YOWmtZYQesCwTLGReIDTv4Bh8G9SgGOAiRsL\n" +
                "vQIDAQAB\n" +
                "-----END PUBLIC KEY-----\n";
        OnboardingServiceImpl onboardingService = new OnboardingServiceImpl(new QA());
        String content = "SOME CONTENT TO SIGN";
        byte[] signature = onboardingService.createSignature(content, privateKey);
        String encodedHexSignature = Hex.encodeHexString(signature);
        byte[] decodedHexSignature = Hex.decodeHex(encodedHexSignature.toCharArray());
        onboardingService.verifySignature(content, decodedHexSignature, publicKey);
    }

    @Test
    void rq31_givenValidPrivateKeyAndMatchingPublicKey_VerifySignatureAfterHex_ShouldReturnBeSuccessfulForRealContent() throws Throwable {
        String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCcun3/je3pK4I5\n" +
                "sLzHHPTkIVyPYJMLa308ISOBq/5SOaA0VQgz1/ezEqjy6FN5ThJJfKtRBGF/WxYV\n" +
                "pmLp7IiR/K3VUvr1tUQ/b/amc1rDvapO0SNgsHiZVFCXdFTqyn3GoKx6kiejr0av\n" +
                "dL3zTBuH4hGST72ukCAQ3XKcQl16KreErt/HsYqo4BD3bsUmP4cD4FWAy43YkGsK\n" +
                "aTm+9Kgo543L2umNrAgsFKJCdP+WFeZeKzJFLAsfyriwnEqdcs/gt0NvNN+YtSBZ\n" +
                "ffKo+DcoziOFtCMUL3uPK4QU2jue315lofpg5aa1lhB6wLBMsZF4gNO/gGHwb1KA\n" +
                "Y4CJGwu9AgMBAAECggEAJ3NYAOpIbw9pHb1rn1EeIYh2RI7iJJc/dFHBHsRc5XSk\n" +
                "61dWE7qymmxBg7dQP7ITr1TGyMNqCUUzx2pU7p3/+INafF6BVjT1A2Ta+KYDvE6z\n" +
                "HljNZ2q0SKAtExqQv3MN+t/52naMrMqTZ9nicGoBahBRLPuOevDDaYtcbJHV9siB\n" +
                "/cuPWgQwHhosCFv7qL5BtCUHoZ2H2eFJ5cGM0QV4slU7+K5tFR7mf9soPfOdiXli\n" +
                "okSW0cX/rVXEXQIplm8DYQz09Kb5txSkMTcGFLX2Rn24pzUVlLERZD3cRIHLJO88\n" +
                "Y+/TG2ND6oHRWBLhvK43xxnWXy6M7VVLvGuXLZwxbwKBgQDNwTCZMZ/SyFtNksTM\n" +
                "zUq3ATG3+47hZ2KoCyOjpqWDpcWNA/DF87H6aV7ARpAh6E+ku70AXNReDv0DOn/t\n" +
                "PVfjfWNkXNVeRz8r9E+A323gQniMzG96smALaCKV9eryQog3PmklrKfX/DWmSV8w\n" +
                "NQo8Y/ckC8kVcC6k81mBRJkpywKBgQDDAGr57IWXp/72DX0lEXDcK7OTse13zg/u\n" +
                "Uu68eJDKPpbEUIpi3JcHWPUmYTtNyFvWRIxUle9CzkDqKGDmhKZIK7w/kk7lC+6+\n" +
                "+aiV5x4tFgXGcauQVvfmsWyox1r9buUEe3neNLCH2OAcUrIJSEAWutzbnHR/fRdX\n" +
                "7q0QO+GPlwKBgAm6t+yWjyMtfDvH0+yHTKAou0wi5he8hsrBdBb5+1ulTBkl9Y22\n" +
                "v1TDBMr8yzX/QTzd78sDmLs15Drx2jjCrNRCCqAlT6Wcot0WOWgzqINxQTgPNrpL\n" +
                "kwUXEIOc1FZOVRifp+MzLeCluXe6gnjKGUjPxVD8Ca8s1dfQ9guJzeBPAoGAZoTW\n" +
                "uRR/W68H3SZUT+OueSJEo/acSSs9tudE+jNMs/dZ3lYVm0or4MIFCKt/uTIIDks1\n" +
                "l9bH1lCHUlPuNeMxZKoRqz5pWgP+/cpVtpGujLeSS7VBJH6EkHVhEg7VKH5fyw57\n" +
                "kLYUxSbhFCLp1PV2ND0DljXYDZqfZixmMi25NXsCgYAO3ZOrObDLJpEDCFo6ArEr\n" +
                "YCBM03ETtFMnd+zDJxY2REhYh+HWgHZ0UvGPB9kYslT9MwvJNNKPnmcQP5n5J5RF\n" +
                "YE6rVQRynPGy7+gAqB0omgknPKHzKMKZChWDiY0gN85s6wHNsf7u5qgnj5b/k+Zq\n" +
                "+0h0jlv7SvLRdCo5vjbVkw==\n" +
                "-----END PRIVATE KEY-----\n";
        String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnLp9/43t6SuCObC8xxz0\n" +
                "5CFcj2CTC2t9PCEjgav+UjmgNFUIM9f3sxKo8uhTeU4SSXyrUQRhf1sWFaZi6eyI\n" +
                "kfyt1VL69bVEP2/2pnNaw72qTtEjYLB4mVRQl3RU6sp9xqCsepIno69Gr3S980wb\n" +
                "h+IRkk+9rpAgEN1ynEJdeiq3hK7fx7GKqOAQ927FJj+HA+BVgMuN2JBrCmk5vvSo\n" +
                "KOeNy9rpjawILBSiQnT/lhXmXisyRSwLH8q4sJxKnXLP4LdDbzTfmLUgWX3yqPg3\n" +
                "KM4jhbQjFC97jyuEFNo7nt9eZaH6YOWmtZYQesCwTLGReIDTv4Bh8G9SgGOAiRsL\n" +
                "vQIDAQAB\n" +
                "-----END PUBLIC KEY-----\n";
        OnboardingServiceImpl onboardingService = new OnboardingServiceImpl(new QA());
        String requestBody = "{\"id\":\"com.dke.data.agrirouter.testmanagement.integration.requirements.onboard.SecuredOnboardingTest\",\"applicationId\":\"12e450c0-cab9-4ad0-8537-841a3b6ee1aa\",\"certificationVersionId\":\"cc5d513f-45e9-455c-be81-bb697d7e071b\",\"gatewayId\":\"3\",\"certificateType\":\"PEM\",\"UTCTimestamp\":\"2018-07-03T15:38:21.240122Z\",\"timeZone\":\"+02:00\"}";
        byte[] signature = onboardingService.createSignature(requestBody, privateKey);
        String encodedHexSignature = Hex.encodeHexString(signature);
        byte[] decodedHexSignature = Hex.decodeHex(encodedHexSignature.toCharArray());
        onboardingService.verifySignature(requestBody, decodedHexSignature, publicKey);
    }

    @Test
    void rq31_givenValidPublicKey_VerifySignatureFromRequest_ShouldBeSuccessfulForRealContent() throws Throwable {
        String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnLp9/43t6SuCObC8xxz0\n" +
                "5CFcj2CTC2t9PCEjgav+UjmgNFUIM9f3sxKo8uhTeU4SSXyrUQRhf1sWFaZi6eyI\n" +
                "kfyt1VL69bVEP2/2pnNaw72qTtEjYLB4mVRQl3RU6sp9xqCsepIno69Gr3S980wb\n" +
                "h+IRkk+9rpAgEN1ynEJdeiq3hK7fx7GKqOAQ927FJj+HA+BVgMuN2JBrCmk5vvSo\n" +
                "KOeNy9rpjawILBSiQnT/lhXmXisyRSwLH8q4sJxKnXLP4LdDbzTfmLUgWX3yqPg3\n" +
                "KM4jhbQjFC97jyuEFNo7nt9eZaH6YOWmtZYQesCwTLGReIDTv4Bh8G9SgGOAiRsL\n" +
                "vQIDAQAB\n" +
                "-----END PUBLIC KEY-----\n";

        String expectedSignature = "3cc7ba088f8b6cfe28605d76468b467019c04c9701060f40aa1f79e667723d30767b9b46d82c1c72dabb5087c966af1995c036e6cc491cd12a20683be46a725f0e3af999256290a5dc380cdf54b4900aafa5bc3f7f17afeccfa3a11fa4b9fe867ac23ef7fd7e41efbe32910dc36214e8887e1524773083ee98174d7782561851e8de85a7db5ae493a16f4be5c83f5c91ac74347b3d3bd2942d4ff82575904e8f4cb81fb5f970ecc9a0b50de52bf70d242a13d6b6cf0970fc5ea119d826e40c4bca17766b5d2d6efdd40cfe337f5a4114b9e8783aa93b0ba0fa7e3327a6d6e5e17fc241c952ca5d8b247776adee267398a0523aae87a5c1328460b526b489767a";
        String requestBody = "{\"id\":\"com.dke.data.agrirouter.testmanagement.integration.requirements.onboard.SecuredOnboardingTest\",\"applicationId\":\"12e450c0-cab9-4ad0-8537-841a3b6ee1aa\",\"certificationVersionId\":\"cc5d513f-45e9-455c-be81-bb697d7e071b\",\"gatewayId\":\"3\",\"UTCTimestamp\":\"2018-07-05T11:00:38.409910800Z\",\"timeZone\":\"+02:00\",\"certificateType\":\"PEM\"}";

        OnboardingServiceImpl onboardingService = new OnboardingServiceImpl(new QA());

        onboardingService.verifySignature(requestBody, Hex.decodeHex(expectedSignature.toCharArray()), publicKey);
    }

}
