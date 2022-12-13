import { Router } from "@angular/router";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { FormsModule, ReactiveFormsModule, FormGroup } from "@angular/forms";
import { By } from "@angular/platform-browser";
import { of } from "rxjs";

import { Message } from "./../shared/message";
import { DataService } from "./../shared/services/data.service";
import { LoginComponent } from "./login.component";
import { LoginService } from "./service/login.service";

describe("LoginComponent", () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  let loginServiceSpy: jasmine.SpyObj<LoginService>;
  let dataServiceSpy: jasmine.SpyObj<DataService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    loginServiceSpy = jasmine.createSpyObj("LoginService", ["logar"]);
    dataServiceSpy = jasmine.createSpyObj("DataService", [
      "setLogado",
      "setContaGrafica",
    ]);
    routerSpy = jasmine.createSpyObj("Router", ["navigateByUrl"]);

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, FormsModule],
      declarations: [LoginComponent],
      providers: [
        { provide: LoginService, useValue: loginServiceSpy },
        { provide: DataService, useValue: dataServiceSpy },
        { provide: Router, useValue: routerSpy },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    component.ngOnInit();
    fixture.detectChanges();
  });

  describe('Initial state', () => {
    it("should create", () => {
      expect(component).toBeTruthy();
    });
  
    it("should reset the dataService values", () => {
      expect(dataServiceSpy.setLogado).toHaveBeenCalledWith(false);
      expect(dataServiceSpy.setContaGrafica).toHaveBeenCalledWith("");
    });
  });

  describe("Input", () => {
    let usuarioInputElement: HTMLInputElement;
    let senhaInputElement: HTMLInputElement;

    beforeEach(() => {
      usuarioInputElement = fixture.debugElement.query(By.css("#usuario")).nativeElement;
      senhaInputElement = fixture.debugElement.query(By.css("#senha")).nativeElement;
    })

    it("should have empty user and password fields", () => {
      expect(usuarioInputElement.value).toBe("");
      expect(senhaInputElement.value).toBe("");

      expect(component.forms.valid).toBeFalsy();
    });

    it("should receive user and password", () => {
      const expected = { usuario: "abcde", senha: "secret" };

      inputValue(usuarioInputElement, expected.usuario);
      inputValue(senhaInputElement, expected.senha);

      fixture.detectChanges();

      expect(component.forms.valid).toBeTruthy();
      expect(component.forms.value).toEqual(expected);
    });

    function inputValue(field: HTMLInputElement, value: string): void {
      field.value = value;
      field.dispatchEvent(new Event("input"));
    }
  });

  describe("On login", () => {
    const successMsg = new Message("Usuário logado com sucesso!");
    const failMsg = new Message("Usuário ou senha inválidos!");

    it("should send the form data to loginService", () => {
      const form = fillLoginForms("abcde", "secret", component.forms);

      loginServiceSpy.logar.and.returnValue(of(new Message("")));
      component.onSubmit();
      expect(loginServiceSpy.logar).toHaveBeenCalledWith(form);
    });

    it('should (on success) set isLogado to true and navigate to "/grafica"', () => {
      loginServiceSpy.logar.and.returnValue(of(successMsg));
      component.onSubmit();
      expect(dataServiceSpy.setLogado).toHaveBeenCalledWith(true);
      expect(routerSpy.navigateByUrl).toHaveBeenCalledWith("/grafica");
    });

    it("should (on failure) show a failure message", () => {
      loginServiceSpy.logar.and.returnValue(of(failMsg));
      component.onSubmit();

      fixture.detectChanges();

      expect(
        fixture.debugElement.query(By.css(".error-msg")).nativeElement
          .textContent
      ).toBe(failMsg.message);
    });

    function fillLoginForms(
      usuario: string,
      senha: string,
      formGroup: FormGroup
    ): FormData 
    {
      formGroup.controls["usuario"].setValue(usuario);
      formGroup.controls["senha"].setValue(senha);

      const form = new FormData();
      form.append("usuario", usuario);
      form.append("senha", senha);
      return form;
    }
  });
});