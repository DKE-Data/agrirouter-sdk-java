package com.dke.data.agrirouter.test;

import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.env.QA;

public abstract class AbstractIntegrationTest {

  private String getAccountId() {
    return "5d47a537-9455-410d-aa6d-fbd69a5cf990";
  }

  protected String getApplicationId() {
    return "39d18ae2-04e3-42de-8a42-935565a6b261";
  }

  protected String getCertificationVersionId() {
    return "719afec8-d2ff-4cf8-8194-e688ae56b3b5";
  }

  protected Environment getEnvironment() {
    return new QA() {};
  }
}
