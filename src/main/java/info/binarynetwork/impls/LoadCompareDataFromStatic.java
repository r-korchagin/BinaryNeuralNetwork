package info.binarynetwork.impls;

import org.springframework.stereotype.Component;

import info.binarynetwork.interfaces.LoadCompareData;
import info.binarynetwork.objects.CompareData;

@Component("compareStatic")
public class LoadCompareDataFromStatic implements LoadCompareData {

	private CompareData staticData;

	public LoadCompareDataFromStatic() {
		staticData = new CompareData();
		staticData.setData("В Одессе найден мертвым прокурор района ", 0.75);
		staticData.setData("В Днепропетровске люди возмущены новыми ценами на проезд: Нам остается только выживать ", 0.66);
		staticData.setData("Удовлетворенность россиян своей жизнью упала на шесть пунктов ", 0.66);
		staticData.setData("Заседание Рады закрыто из-за драки между Ляшко и Мельничуком ", 0.66);
		staticData.setData("Нацбанк повысил ставку рефинансирования с завтрашнего дня до 30%", 0.66);
		staticData.setData("В Раде произошла драка с участием Ляшко и комбата Айдара", 0.66);
		staticData.setData("Путин назвал издевательством реформы в Украине", 0.66);
		staticData.setData("Сенаторы решили не спешить с сокращением своих зарплат ", 0.66);
		staticData.setData("Отопительный сезон в Украине может завершиться раньше срока ", 0.66);
		staticData.setData("Китай в 2015 году выделит $73 млрд на строительство инфраструктуры ", 0.66);
		staticData.setData("Планы Рады на день: армия для долгой войны, борьба с терроризмом и новый выходной день", 0.66);
		staticData.setData("Украинцы в отчаянии из-за новых тарифов ", 0.66);
		staticData.setData("В Сеуле вооруженный ножом мужчина напал на посла США", 0.66);
		staticData.setData("В Черное море вошли шесть военных кораблей НАТО", 0.66);
		staticData.setData("Обама пока не принял решения о поставках оружия в Украину - Нуланд ", 0.25);
		staticData.setData("Пхеньян пригрозил США превентивным ударом ", 0.33);
		staticData.setData("Девальвация гривны, война и паника. Профессиональное развенчание мифов", 0.33);
		staticData.setData("Фирташ презентовал фонд по модернизации Украины", 0.33);
		staticData.setData("Украинские военные заявили о возвращении техники ополченцев под Луганск", 0.33);
		staticData.setData("США ни при чем. Ученые выяснили причину гражданской войны в Сирии", 0.33);
		staticData.setData("Путин: юбилей Победы священен для России и Белоруссии", 0.33);
		staticData.setData("Fitch: нефтетрейдеры увеличат прибыль в 2015 году из-за волатильности", 0.33);
		staticData.setData("В РФ заявили, что долг Нафтогаза за российский газ составляет $2,4 млрд", 0.33);
		staticData.setData("Запад готов к решительной реакции в случае нарушения минских соглашений", 0.33);
		staticData.setData("Александр Железняк: «Малый бизнес поднимет экономику»", 0.33);
		staticData.setData("На Украине началась подготовка в конституционной реформе", 0.33);
		staticData.setData("США обещают Сноудену честный суд", 0.33);
		staticData.setData("Пентагон: Если Россия нарушит ракетный договор, США примут оборонительные меры", 0.33);
		staticData.setData("РПЦ работает в Украине как политическая сила — эксперт", 0.33);
	}

	public CompareData loadInputData() {
		return this.staticData;
	}

}
