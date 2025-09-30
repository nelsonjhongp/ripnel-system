package com.ripnel.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {
    @GetMapping("/admin")      public String admin(){ return "admin"; }
    @GetMapping("/almacen")    public String almacen(){ return "almacen"; }
    @GetMapping("/compras")    public String compras(){ return "compras"; }
    @GetMapping("/produccion") public String produccion(){ return "produccion"; }
    @GetMapping("/ventas")     public String ventas(){ return "ventas"; }
}
