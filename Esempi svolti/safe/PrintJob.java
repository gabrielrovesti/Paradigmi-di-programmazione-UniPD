package it.unipd.pdp2021.safe;

import java.util.Date;

/** Represents a print job. */
class PrintJob {
  final Date enqueued;
  final Object document;

  PrintJob(Object document) {
    this.enqueued = new Date();
    this.document = document;
  }
}
