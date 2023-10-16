package com.bsdev.electionsapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ElectionsController {

  private final ElectionsService electionsService;

  public ElectionsController(ElectionsService electionsService) {
    this.electionsService = electionsService;
  }

  @GetMapping("/elections")
  public String getElecetionData() {
    return electionsService.getPageText();
  }

  @GetMapping("/test")
  public String test() {
    return "TEST";
  }

}
