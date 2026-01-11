package com.example.nl2sqlui.web;

import com.example.nl2sqlui.dto.ExecuteResponse;
import com.example.nl2sqlui.dto.GenerateResponse;
import com.example.nl2sqlui.service.ContextPreviewService;
import com.example.nl2sqlui.service.FastApiClient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UiController {

    private final FastApiClient fastApi;
    private final ContextPreviewService contextPreview;

    public UiController(FastApiClient fastApi, ContextPreviewService contextPreview) {
        this.fastApi = fastApi;
        this.contextPreview = contextPreview;
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "text", required = false) String text) {
        model.addAttribute("text", text == null ? "" : text);
        model.addAttribute("maxRows", 200);
        model.addAttribute("doExecute", true);

        Optional<String> ctx = contextPreview.getContextText();
        model.addAttribute("context", ctx.orElse(""));
        model.addAttribute("contextEnabled", ctx.isPresent());

        return "index";
    }

    @PostMapping("/run")
    public String run(Model model,
                      @RequestParam("text") @NotBlank String text,
                      @RequestParam(value = "doExecute", defaultValue = "true") boolean doExecute,
                      @RequestParam(value = "maxRows", defaultValue = "200") @Min(1) int maxRows) {

        model.addAttribute("text", text);
        model.addAttribute("maxRows", maxRows);
        model.addAttribute("doExecute", doExecute);

        Optional<String> ctx = contextPreview.getContextText();
        model.addAttribute("context", ctx.orElse(""));
        model.addAttribute("contextEnabled", ctx.isPresent());

        try {
            GenerateResponse gen = fastApi.generate(text);
            model.addAttribute("gen", gen);

            if (doExecute && gen != null && gen.isIs_valid()) {
                ExecuteResponse exec = fastApi.execute(text, maxRows);
                model.addAttribute("exec", exec);
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "index";
    }
}
