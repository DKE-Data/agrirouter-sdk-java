package com.dke.data.agrirouter.test;

import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.env.QA;
import org.apache.commons.lang3.NotImplementedException;

import java.util.concurrent.TimeUnit;

/**
 * Abstract implementation for all integration tests.
 */
public abstract class AbstractIntegrationTest {

    /**
     * Since there are multiple problems with the stability of the QA env we define dedicated
     * constants for message fetching.
     */
    protected final int MAX_TRIES_BEFORE_FAILURE = 10;

    protected final long DEFAULT_INTERVAL = 5000;

    /**
     * Wait for the AR to process the messages. Since the QA has some stability problems, we will wait
     * up to 30 seconds until the AR has processed the messages.
     *
     * @throws InterruptedException -
     */
    protected void waitForTheAgrirouterToProcessSingleMessage() throws InterruptedException {
        Thread.sleep(TimeUnit.SECONDS.toMillis(30));
    }

    /**
     * Wait for the AR to process the messages. Since the QA has some stability problems, we will wait
     * up to one minute until the AR has processed the messages.
     *
     * @throws InterruptedException -
     */
    protected void waitForTheAgrirouterToProcessMultipleMessages() throws InterruptedException {
        Thread.sleep(TimeUnit.SECONDS.toMillis(60));
    }

    /**
     * Communication unit for integration testing.
     */
    protected final Application communicationUnit =
            new Application() {
                @Override
                public String getApplicationId() {
                    return "8af89642-5e30-11ef-a95b-d2e1bbd0b244";
                }

                @Override
                public String getCertificationVersionId() {
                    return "96c2c3e8-5e30-11ef-a95b-d2e1bbd0b244";
                }

                @Override
                public Environment getEnvironment() {
                    return new QA() {
                    };
                }
            };

    /**
     * Farming software for integration testing.
     */
    protected final Application farmingSoftware =
            new Application() {

                @Override
                public String getApplicationId() {
                    return "2bb59356-5e31-11ef-a95b-d2e1bbd0b244";
                }

                @Override
                public String getCertificationVersionId() {
                    return "4dc7a506-5e31-11ef-a95b-d2e1bbd0b244";
                }

                @Override
                public Environment getEnvironment() {
                    return new QA() {
                    };
                }

                @Override
                public String getPrivateKey() {
                    return """
                            -----BEGIN PRIVATE KEY-----
                            MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDRFJg/FMUntssw
                            Zy2/7KM3PM09DPbt7T4MFFM7/A6oUQPePm4EbBU2i2U7LRlVosE93F0zqo/ie7xt
                            yjkUmZZcaYP4+kRbzEJ2kANlZ44W0b0SRLx5VQ3aaQh7klo7VV/+NNToZ0p5Z+L7
                            kBiOlgLPlCmZMloa0bStQPiOzmKgiyPhJp326hQUJ6Cl9oExhT/w+oFjygrrXE4c
                            B27kD4tve3Ai8X9ZfV1fMihexpCcvwowHN3MB376bkI++CxK8P+WI4MS46qMSsY2
                            X4i/hGVevUF7tcykeYDFiVjaSDcWtBReG536gmB86Imhu+kCZPxz67hk6+z/EFKA
                            d0OQqGOBAgMBAAECggEAHlycQ0TT4JGF7jB9j0pg5UuaDkyMqz5xbTWiZKqUwlKJ
                            ekOcnF3JstMJT0+cRzhMUZxnmHIjVONdGKBuK5SmJiNYe9Z0CNSfrjH5nEcudhfd
                            GgMLT59PVLE9/xvgqI8cUoD/jgQ552lzp85kEoisPdKpO8r5FL3xO9ucfLhx0BhB
                            WdQZcnqAvMHrlMwuWCXzdqag3nAF7x+heNklVuhIrKGhy7r9ujmv2xf0ccpzfiSq
                            AC6p/BLgjdXRrZsGKZ/f5mL584RkhFhCfY8U7MURY4w1Qx73mAvY7DzPZ82cmjPf
                            D0HPGF9lNQNYeNP6LTZ6+wFLYhsJF00ciXAyeFs4RQKBgQDqJ6tLzQ3YRfk/wTAE
                            WLSxmtKsn7xWh9YhOiOs4vgGd4Ap0kcj4vzOgolNdqUDDTrVi/lyA3r26lzSsrbr
                            Tv/LzuhbTAEa+AlbfVaoimqRkz7oVSDVEeofLTiAm6KOVRCjcxSAKsZzCoZ0MWJl
                            Aq3U4XtjSq0gAzxiao5BrhwUuwKBgQDklhH55D8VHgb9KKznFw0yg2XK/+hAO+Lo
                            aTP+QdJRpbBEozmVWB8uPu116DALRYoQ16jPUU7xODf38UT/w1F8xpa8+UINXfO1
                            nXl2hjqqwcs0V/1YAYHutMFmlDaN8V8w0FIFdnggOLkkvWdlP3nRslV5/1yZixZ2
                            yDt7nbHC8wKBgFYkTeJPB57e7zlDKxUORTtph8O16CW1R1Nl/iXmQfSvFdlM4JsV
                            MIgQgc6brU0YGuLg4d3N5fjcc1WYJa8fmv8xmU2tAmMXiOm9jJnoghgYIXZ7it6X
                            /peFnVnC5+SQ6tQWzB3ynptH7frlCBu9BpJgeXIn0NUMG7ThGoCTOwVtAoGAHwLI
                            /4jakgfX1OJ+yn8TGEZBOhALtyMovnT/S8AsQ01iI32tYzhoSoLY/kT4t57Ag1Zu
                            nWmfdxQ0a5u8y6H43R+n9Y0vS9byKDJr9wtwzBesdLZ3NUhHE1ZMYpk66XjUfC/W
                            4MzYsS5g174mV9MLuMoj1IAzVNVMv+KcR0H2iscCgYEA1uVd+razCxfq/4qRN5ke
                            BwPdbJx1m7V0dVHNJm6q+Ig7L3ovbJt88599T0Nf7WyEhbUaXVWKkH/OxCvesLX3
                            b0VMOte/UYuSoGiOuOmzFoQoR1IAjqlC0vHqfzMJRGvOT9k/ZHvI2HwlHax0wcTI
                            wy+YtX0o2eOcr4QVO+M/kOI=
                            -----END PRIVATE KEY-----
                            """;
                }

                @Override
                public String getPublicKey() {
                    return """
                            -----BEGIN PUBLIC KEY-----
                            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0RSYPxTFJ7bLMGctv+yj
                            NzzNPQz27e0+DBRTO/wOqFED3j5uBGwVNotlOy0ZVaLBPdxdM6qP4nu8bco5FJmW
                            XGmD+PpEW8xCdpADZWeOFtG9EkS8eVUN2mkIe5JaO1Vf/jTU6GdKeWfi+5AYjpYC
                            z5QpmTJaGtG0rUD4js5ioIsj4Sad9uoUFCegpfaBMYU/8PqBY8oK61xOHAdu5A+L
                            b3twIvF/WX1dXzIoXsaQnL8KMBzdzAd++m5CPvgsSvD/liODEuOqjErGNl+Iv4Rl
                            Xr1Be7XMpHmAxYlY2kg3FrQUXhud+oJgfOiJobvpAmT8c+u4ZOvs/xBSgHdDkKhj
                            gQIDAQAB
                            -----END PUBLIC KEY-----
                            """;
                }
            };

    /**
     * Farming software for integration testing.
     */
    protected final Application telemetryPlatform =
            new Application() {

                @Override
                public String getApplicationId() {
                    return "e051056d-5e31-11ef-a95b-d2e1bbd0b244";
                }

                @Override
                public String getCertificationVersionId() {
                    return "e743930a-5e31-11ef-a95b-d2e1bbd0b244";
                }

                @Override
                public Environment getEnvironment() {
                    return new QA() {
                    };
                }

                @Override
                public String getPrivateKey() {
                    return """
                             -----BEGIN PRIVATE KEY-----
                             MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQD38PmCfYHaoPRF
                             J47yIxCVmGAg94mnSV5C4XGSOXq1ha/GCjhsR+YDgKnBt+lenxGt4TExN1OOK/SS
                             uconfh61o1ouUPbaq3OImlSecYGIFzOQxWj9q97Yp+SNek9GDKj8VgUK33atL82W
                             YbY3QO/clh41zHDJ8fMObZ+2setfdYaQkxOFxqVLBu5TDg7CTOFfgdFXdgX7e2Qu
                             C4p8YiTYO73YoqX/Rcnsc6tNbWA5lwqSDoaVXn3BHcjN9VDPMjDSWyJKq9cCVDus
                             Xs4uB5Nqv1MFiZoZQHz8Cvva8JbXVmET3sbPl12RCLflYi21q9NvUgUI36rPSykO
                             bTwu0duhAgMBAAECggEAYivCz10cdlF2ho80NUGXBnRv033PvjH5paeYDVq+8HK/
                             dz2Cu7T4KPjfvjkwqOt4140rqh4K8mcLDrGLjnmMdUT5pw8dvtVWCId668VrkVQG
                             cdMIUDCmKsjHYRFFpCnNGySAGJ8EYWxlDZ8gw93tWz0BNzH8kqk/IL5dF7x4wrGB
                             kKdVybTjAJK6WEj7qcRriqER9HfFanhU9swQy8P8PbnHuFPVye0GqUfFEvBM0SSP
                             YRITNu+TkQX709We1ipycUhPfwdwPdK6RtBp1SNdXbp+fFjtkdihdwQ/950C7ZF7
                             2S3JVAnOn5lGGiPZGd5554sJfO1xv890FkHW9lpiRQKBgQD/K9Xsh9UfWZmAgsbH
                             dBisPOxRTX+bv4qeJoVaxP8pPvl8Efwkcr792mBz+6kXQdHeWUGrLL2ajsJE8ofr
                             OrfibQztCgMHJY2Smuc757yTIqyJOEVSFdmbi+0s1L/Kd04DAuXwoswB1gx79+ER
                             K33TDtAGP713Nyz28hLoBl+cSwKBgQD4vyCr0hq27j5aAOb7WnnOP0vjSlt/gvwJ
                             Ih8L6oWPs9YWTrhIVNPmcCX1AaAj5hZmcR4ve4DUWmLi1YsGshdZZf+fIDZhGYw+
                             639wC7MlcahqkliJN3XN1rNmRgpWVqgJWsJ+DtnpzK9dclgHdcCdb4YPPd9vW1/A
                             x1euwENcQwKBgQDmimlVsYdpMp+QTxdfHtIn00dDUXyIb+7CYlU7pB5Q2pe3c9xV
                             5eIPIlzgjS/BVnNRq1y8VcjcOjzk5CbO7Rc0GIiEJPaFX3VgTlm6cu2aFcrCkzd2
                             KU2Yzjwzs+ev+kheSiELX1poEkbnPhZ3V87vgEOMUcxRSGV0m1NflFqPrwKBgQDs
                             TBxMPQZVutMOiwbhxhwp+mUr4Oo0ooisYHafykTgs1Tv0g4r4DGAGg9kxQuMrrme
                             zVyAfNFwaQDBPVyZmrqlmUiQsHgYG73n0603ElY3ftYfjzB+AWpuhMPdpzGQvTdZ
                             05cpSxBlkqUQn1vSkmDM7jGq7vr/fQzkRDoebtROowKBgDYH0nUzMXj+1q7ZEojk
                             DEPfY22pcH6pNrg3RkCi19BVRyUtk/CAJiSNud/wR61Kbq1wFQkqn8dU0ydbsqiX
                             Tz0LofvoLK3cZc6IhmdgOiOs75LIoMOfxskGpFdiNxGId8EIXACAwomm2S4o8KfI
                             fagxljG8Ic5oHxnVengksyd5
                             -----END PRIVATE KEY-----
                            """;
                }

                @Override
                public String getPublicKey() {
                    return """
                            -----BEGIN PUBLIC KEY-----
                            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA9/D5gn2B2qD0RSeO8iMQ
                            lZhgIPeJp0leQuFxkjl6tYWvxgo4bEfmA4CpwbfpXp8RreExMTdTjiv0krnKJ34e
                            taNaLlD22qtziJpUnnGBiBczkMVo/ave2KfkjXpPRgyo/FYFCt92rS/NlmG2N0Dv
                            3JYeNcxwyfHzDm2ftrHrX3WGkJMThcalSwbuUw4OwkzhX4HRV3YF+3tkLguKfGIk
                            2Du92KKl/0XJ7HOrTW1gOZcKkg6GlV59wR3IzfVQzzIw0lsiSqvXAlQ7rF7OLgeT
                            ar9TBYmaGUB8/Ar72vCW11ZhE97Gz5ddkQi35WIttavTb1IFCN+qz0spDm08LtHb
                            oQIDAQAB
                            -----END PUBLIC KEY-----
                            """;
                }
            };

    /**
     * Representation of an application.
     */
    public abstract static class Application {

        /**
         * Application for all the test cases.
         *
         * @return -
         */
        public abstract String getApplicationId();

        /**
         * Version for all the test cases.
         *
         * @return -
         */
        public abstract String getCertificationVersionId();

        /**
         * Environment for the application.
         *
         * @return -
         */
        public abstract Environment getEnvironment();

        /**
         * Private key for the application.
         *
         * @return -
         */
        public String getPrivateKey() {
            throw new NotImplementedException("This application does not support private keys.");
        }

        /**
         * Public key for the application.
         *
         * @return -
         */
        public String getPublicKey() {
            throw new NotImplementedException("This application does not support private keys.");
        }
    }
}
