import { TestBed } from '@angular/core/testing';

import { GetEmailsService } from './get-emails.service';

describe('GetEmailsService', () => {
  let service: GetEmailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GetEmailsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
