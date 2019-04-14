package com.eventoapp.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;
import com.eventoapp.repository.ConvidadoRepository;
import com.eventoapp.repository.EventoRepository;

@Controller 
//@CrossOrigin
public class EventoController {
	
	@Autowired
	private EventoRepository repositorioEvento;
	
	@Autowired
	private ConvidadoRepository repositorioConvidado;
	
	@RequestMapping(value="/cadastrarEvento", method = RequestMethod.GET)
	public String form() {
		return "evento/formEvento";
	}
	
	
	@RequestMapping(value="/cadastrarEvento", method = RequestMethod.POST)
	public String form(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
		
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/cadastrarEvento";
		}
		this.repositorioEvento.save(evento);
		attributes.addFlashAttribute("mensagem", "Evento armazenado com sucesso!");
		return "redirect:/cadastrarEvento";
	}
	
	@RequestMapping(value ="/eventosRest")
	public ResponseEntity<Iterable<Evento>> listaEventosRest() {
		ModelAndView model = new ModelAndView("index");
		Iterable<Evento> eventos = this.repositorioEvento.findAll();
		model.addObject("eventos", eventos);
		
		return ResponseEntity.ok().body(eventos);
		
	}
	
	@RequestMapping(value ="/eventos")
	public String listaEventos(Model model) {
		
		Iterable<Evento> eventos = this.repositorioEvento.findAll();
		model.addAttribute("eventos", eventos);
		
		return "index";
		
	}
	
	@RequestMapping(value="/{codigo}", method = RequestMethod.GET)
	public String detalhesEvento(@PathVariable("codigo") Long codigo, Model model) {
		if(codigo != null) {
			Evento evento = this.repositorioEvento.findByCodigo(codigo);
			Iterable<Convidado> convidados = this.repositorioConvidado.findByEvento(evento);
			model.addAttribute("convidados", convidados);
			model.addAttribute("evento", evento);
			return "evento/detalhesEvento";
		}
		return "index";
	}
	
	@RequestMapping(value="/{codigo}", method = RequestMethod.POST)
	public String detalhesEventoPost(@PathVariable("codigo") Long codigo, @Valid Convidado convidado, 
			BindingResult result, RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return  "redirect:/{codigo}";
		}
		if(codigo != null && convidado != null) {
			Evento evento = this.repositorioEvento.findByCodigo(codigo);
			convidado.setEvento(evento);
			this.repositorioConvidado.save(convidado);
			redirectAttributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
			return  "redirect:/{codigo}";
		}
		return "evento/detalhesEvento";
	}
	
//	@RequestMapping(value="/deletar/{codigo}", method = RequestMethod.GET)
//	public String DeletarEvento(@PathVariable("codigo") Long codigo, RedirectAttributes attributes) {
//		Evento evento = this.repositorioEvento.findByCodigo(codigo);
//		this.repositorioEvento.delete(evento);
//		attributes.addFlashAttribute("mensagem", "Evento removido com sucesso!");
//		return "redirect:/eventos";
//	}
	
	@RequestMapping("deletarEvento")
	public String DeletarEvento(Long codigo) {
		Evento evento = this.repositorioEvento.findByCodigo(codigo);
		this.repositorioEvento.delete(evento);
		return "redirect:/eventos";
	}
	
	@RequestMapping("/deletarConvidado")
	public String deletarConvidado(String rg) {
		Convidado convidado = this.repositorioConvidado.findByRg(rg);
		this.repositorioConvidado.delete(convidado);
		Long codigoEvento = convidado.getEvento().getCodigo();
		return "redirect:/"+codigoEvento;
	}
	
}
