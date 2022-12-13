import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { Message } from 'src/app/shared/message';

import { LoginService } from './login.service';

describe('LoginService', () => {
  let service: LoginService;
  let spyHttp: jasmine.SpyObj<HttpClient>

  beforeEach(() => {
    spyHttp = jasmine.createSpyObj('HttpClient', ['post']);
    service = new LoginService(spyHttp);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  const successMsg = new Message('usuario logado com sucesso!');
  const failureMsg = new Message('usuario ou senha inválidos!');
  
  it('should (on success) make a post request and return a success response', () => {
    const userForm = new FormData();
    userForm.append('usuario', '123abc');
    userForm.append('senha', 'Secret');

    spyHttp.post.and.returnValue(of(successMsg));

    service.logar(userForm).subscribe(
      msg => {
        expect(msg).withContext('expected message').toEqual(successMsg)
      }
    )

    expect(spyHttp.post).toHaveBeenCalledWith(service.api, userForm);
  });

  it('should (on failure) make a post request and return a fail response', () => {
    const userForm = new FormData();
    userForm.append('usuario', '123abc');
    userForm.append('senha', 'Secret');

    spyHttp.post.and.returnValue(of(failureMsg));

    service.logar(userForm).subscribe(
      msg => {
        expect(msg).withContext('expected message').toEqual(failureMsg)
      }
    )

    expect(spyHttp.post).toHaveBeenCalledWith(service.api, userForm);
  });
});
