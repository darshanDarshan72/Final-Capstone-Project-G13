import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  query!:string

  constructor() { }

  ngOnInit(): void {
  }

  isLoggedIn()
  {
    const username = window.localStorage.getItem("username");
    if(username == null || username == undefined)
    {
      return false;
    }
    return true;

  }

  returnDash()
  {
    const role = window.localStorage.getItem("roles");
    if(role == "ROLE_USER,ROLE_ADMIN")
    {
      return "/admin_dashboard";
    }
    else
    {
      return "/user_dashboard";
    }
  }

  isAdmin()
  {
    const role = window.localStorage.getItem("roles");
    if(role == "ROLE_USER,ROLE_ADMIN")
    {
      return true;
    }
    else
    {
      return false;
    }

  }

  getUser()
  {
    return window.localStorage.getItem("username");
  }



}
