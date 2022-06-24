package com.example.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.RegistUrl;
import com.example.domain.User;
import com.example.form.EmailSubmitForm;
import com.example.service.RegistUserService;

@Controller
@RequestMapping("/user")
public class RegistUserController {
	@Autowired
	private RegistUserService service;

	@ModelAttribute
	public EmailSubmitForm setUpForm() {
		return new EmailSubmitForm();
	}

	@RequestMapping("")
	public String index(Model model) {
		return "email_submit.html";
	}

	@RequestMapping("/email-submit")
	public String EmailSubmit(@Validated EmailSubmitForm form, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {

		if (result.hasErrors()) {
			return index(model);
		}

		String email = form.getMailAddress();
		List<RegistUrl> registUrlList = null;
		List<User> userList = null;
		registUrlList = service.findRegistUrl(email);
		userList = service.findByMailAddress(email);
		// 現在日時
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());


		// 有効なURLがはっこうされていたらエラー
		if (registUrlList != null) {
			// 登録日時
			Timestamp registDateTime = registUrlList.get(0).getRegistDate();
			// 24時間後
			LocalDateTime registLocalDate = registDateTime.toLocalDateTime();
			registLocalDate.plusHours(24);
			// timestamp型に戻す
			Timestamp registDateTime24 = Timestamp.valueOf(registLocalDate);

			Integer deleted = registUrlList.get(0).getDeleted();
			if (registDateTime24.before(timestamp) && deleted == 0) {

			String mes = "すでに登録URLが発行されています。";
			model.addAttribute("message", mes);
			return index(model);
		}

		}
		// メアド重複の場合完了画面へ
		// userListがnullなら重複チェックしない
		if (userList != null) {
			String registEmail = userList.get(0).getMailAddress();
			if(form.getMailAddress().equals(registEmail)) {
				System.out.println("ユーザー重複");
				return "redirect:/user/email-finished";
			}

		}

		// 重複していない場合
		// key発行
		String uniqueKey = UUID.randomUUID().toString();
		// DB登録
		RegistUrl registUrl = new RegistUrl();
		registUrl.setMailAddress(email);
		registUrl.setUniqueKey(uniqueKey);
		registUrl.setRegistDate(timestamp);
		registUrl.setDeleted(0);
		service.insertRegistUrl(registUrl);
		System.out.println("DB登録");
		// メール送信（service）


		System.out.println("送信完了");
		return "redirect:/user/email-finished";
	}

	@RequestMapping("/email-finished")
	public String EmailFinished() {
		return "email_finished.html";
	}
}
