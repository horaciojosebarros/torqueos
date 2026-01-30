package br.com.torqueos.util;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;

import br.com.torqueos.model.OrdemServico;
import br.com.torqueos.model.PecaOS;
import br.com.torqueos.model.ServicoOS;

public class PdfOrdemServicoGenerator {

  // ============================================================
  // COMPAT: mantém assinatura antiga (não quebra quem já chama)
  // ============================================================
  public static byte[] gerar(OrdemServico os,
                             List<ServicoOS> servicos,
                             List<PecaOS> pecas,
                             String empresaNome,
                             String empresaCnpj) {
    return gerar(os, servicos, pecas,
        empresaNome,
        empresaCnpj,
        null,   // telefone
        null,   // endereco
        null,   // bairro
        null,   // cidade
        null    // uf
    );
  }

  // ============================================================
  // NOVA: cabeçalho completo com dados da empresa
  // ============================================================
  public static byte[] gerar(OrdemServico os,
                             List<ServicoOS> servicos,
                             List<PecaOS> pecas,
                             String empresaNome,
                             String empresaCnpj,
                             String empresaTelefone,
                             String empresaEndereco,
                             String empresaBairro,
                             String empresaCidade,
                             String empresaUf) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      Document doc = new Document(PageSize.A4, 36, 36, 28, 36);
      PdfWriter.getInstance(doc, out);
      doc.open();

      // =========================
      // Fontes
      // =========================
      Font title = new Font(Font.HELVETICA, 15, Font.BOLD);
      Font h = new Font(Font.HELVETICA, 11, Font.BOLD);
      Font normal = new Font(Font.HELVETICA, 10, Font.NORMAL);
      Font small = new Font(Font.HELVETICA, 9, Font.NORMAL);
      Font smallBold = new Font(Font.HELVETICA, 9, Font.BOLD);

      // =========================
      // CABEÇALHO PROFISSIONAL
      // - barra superior
      // - bloco empresa (nome, cnpj, fone, endereço completo)
      // - bloco documento (título, nº OS, data)
      // =========================
      java.awt.Color brand = new java.awt.Color(22, 163, 74);    // verde
      java.awt.Color lightGray = new java.awt.Color(245, 245, 245);
      java.awt.Color textGray = new java.awt.Color(90, 90, 90);

      // barra superior (linha verde)
      PdfPTable topBar = new PdfPTable(1);
      topBar.setWidthPercentage(100);
      PdfPCell bar = new PdfPCell(new Phrase(""));
      bar.setFixedHeight(5f);
      bar.setBorder(Rectangle.NO_BORDER);
      bar.setBackgroundColor(brand);
      topBar.addCell(bar);
      doc.add(topBar);

      doc.add(Chunk.NEWLINE);

      // tabela do header (empresa à esquerda, documento à direita)
      PdfPTable header = new PdfPTable(new float[] { 3.8f, 2.2f });
      header.setWidthPercentage(100);

      // ---- ESQUERDA: empresa
      PdfPCell left = new PdfPCell();
      left.setBorder(Rectangle.NO_BORDER);
      left.setPadding(0);

      // Nome da empresa (bem destacado)
      Paragraph pEmpresa = new Paragraph(safe(empresaNome), new Font(Font.HELVETICA, 13, Font.BOLD));
      pEmpresa.setSpacingAfter(4f);
      left.addElement(pEmpresa);

      // Linha 1: CNPJ e Telefone
      String linhaDoc = joinNonEmpty(" | ",
          prefixIfNotEmpty("CNPJ: ", empresaCnpj),
          prefixIfNotEmpty("Tel: ", empresaTelefone)
      );
      if (!linhaDoc.isEmpty()) {
        Paragraph p1 = new Paragraph(linhaDoc, small);
        p1.getFont().setColor(textGray);
        p1.setSpacingAfter(2f);
        left.addElement(p1);
      }

      // Linha 2: Endereço completo
      String enderecoCompleto = joinEndereco(empresaEndereco, empresaBairro, empresaCidade, empresaUf);
      if (!enderecoCompleto.isEmpty()) {
        Paragraph p2 = new Paragraph(enderecoCompleto, small);
        p2.getFont().setColor(textGray);
        left.addElement(p2);
      }

      header.addCell(left);

      // ---- DIREITA: documento
      PdfPCell right = new PdfPCell();
      right.setBorder(Rectangle.NO_BORDER);
      right.setHorizontalAlignment(Element.ALIGN_RIGHT);
      right.setPadding(0);

      // "chip" do título
      PdfPTable chip = new PdfPTable(1);
      chip.setWidthPercentage(100);

      PdfPCell chipCell = new PdfPCell(new Phrase("ORÇAMENTO / ORDEM DE SERVIÇO", smallBold));
      chipCell.setHorizontalAlignment(Element.ALIGN_CENTER);
      chipCell.setPadding(6f);
      chipCell.setBorder(Rectangle.NO_BORDER);
      chipCell.setBackgroundColor(lightGray);
      chip.addCell(chipCell);

      right.addElement(chip);

      right.addElement(Chunk.NEWLINE);

      String osNum = "Nº " + safe(os != null ? os.getIdOs() : "");
      Paragraph pOs = new Paragraph(osNum, new Font(Font.HELVETICA, 14, Font.BOLD));
      pOs.setAlignment(Element.ALIGN_RIGHT);
      right.addElement(pOs);

      String geradoEm = "Gerado em: " + LocalDateTime.now()
          .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
      Paragraph pData = new Paragraph(geradoEm, small);
      pData.setAlignment(Element.ALIGN_RIGHT);
      pData.getFont().setColor(textGray);
      right.addElement(pData);

      header.addCell(right);

      doc.add(header);

      doc.add(Chunk.NEWLINE);

      // separador fino
      LineSeparator sep = new LineSeparator();
      sep.setLineWidth(0.8f);
      sep.setLineColor(new java.awt.Color(210, 210, 210));
      doc.add(new Chunk(sep));
      doc.add(Chunk.NEWLINE);

      // =========================
      // DADOS PRINCIPAIS
      // =========================
      PdfPTable info = new PdfPTable(new float[] { 1.3f, 3.7f });
      info.setWidthPercentage(100);

      info.addCell(labelCell("Cliente"));
      info.addCell(valueCell(os != null && os.getCliente() != null ? os.getCliente().getNome() : "-", normal));

      info.addCell(labelCell("Veículo"));
      String veic = "-";
      if (os != null && os.getVeiculo() != null) {
        veic = safe(os.getVeiculo().getPlaca()) + " - " + safe(os.getVeiculo().getModelo());
      }
      info.addCell(valueCell(veic, normal));

      info.addCell(labelCell("Status"));
      info.addCell(valueCell(os != null ? safe(os.getStatus()) : "-", normal));

      info.addCell(labelCell("Observações"));
      info.addCell(valueCell(os != null ? safe(os.getObservacoes()) : "", normal));

      doc.add(info);
      doc.add(Chunk.NEWLINE);

      // =========================
      // SERVIÇOS
      // =========================
      doc.add(new Paragraph("Serviços", h));
      doc.add(Chunk.NEWLINE);

      PdfPTable tServ = new PdfPTable(new float[] { 4f, 1.5f });
      tServ.setWidthPercentage(100);
      tServ.addCell(th("Descrição"));
      tServ.addCell(thRight("Valor (R$)"));

      BigDecimal totalServ = BigDecimal.ZERO;

      if (servicos != null && !servicos.isEmpty()) {
        for (ServicoOS s : servicos) {
          tServ.addCell(valueCell(safe(s.getDescricao()), normal));
          BigDecimal v = (s.getValor() != null ? s.getValor() : BigDecimal.ZERO);
          totalServ = totalServ.add(v);
          tServ.addCell(valueCell(formatMoney(v), normal, Element.ALIGN_RIGHT));
        }
      } else {
        PdfPCell c = valueCell("Nenhum serviço", normal);
        c.setColspan(2);
        tServ.addCell(c);
      }

      doc.add(tServ);
      doc.add(Chunk.NEWLINE);

      // =========================
      // PEÇAS
      // =========================
      doc.add(new Paragraph("Peças", h));
      doc.add(Chunk.NEWLINE);

      PdfPTable tPecas = new PdfPTable(new float[] { 3.6f, 0.8f, 1.4f, 1.4f });
      tPecas.setWidthPercentage(100);
      tPecas.addCell(th("Descrição"));
      tPecas.addCell(thRight("Qtd"));
      tPecas.addCell(thRight("Unit (R$)"));
      tPecas.addCell(thRight("Subtotal (R$)"));

      BigDecimal totalPecas = BigDecimal.ZERO;

      if (pecas != null && !pecas.isEmpty()) {
        for (PecaOS p : pecas) {
          int qtd = (p.getQuantidade() != null ? p.getQuantidade() : 0);
          BigDecimal unit = (p.getValorUnitario() != null ? p.getValorUnitario() : BigDecimal.ZERO);
          BigDecimal sub = unit.multiply(new BigDecimal(qtd));
          totalPecas = totalPecas.add(sub);

          tPecas.addCell(valueCell(safe(p.getDescricao()), normal));
          tPecas.addCell(valueCell(String.valueOf(qtd), normal, Element.ALIGN_RIGHT));
          tPecas.addCell(valueCell(formatMoney(unit), normal, Element.ALIGN_RIGHT));
          tPecas.addCell(valueCell(formatMoney(sub), normal, Element.ALIGN_RIGHT));
        }
      } else {
        PdfPCell c = valueCell("Nenhuma peça", normal);
        c.setColspan(4);
        tPecas.addCell(c);
      }

      doc.add(tPecas);
      doc.add(Chunk.NEWLINE);

      // =========================
      // TOTAIS
      // =========================
      BigDecimal total = totalServ.add(totalPecas);

      PdfPTable tot = new PdfPTable(new float[] { 2.2f, 1.3f });
      tot.setWidthPercentage(35);
      tot.setHorizontalAlignment(Element.ALIGN_RIGHT);

      tot.addCell(labelCell("Total serviços"));
      tot.addCell(valueCell(formatMoney(totalServ), normal, Element.ALIGN_RIGHT));

      tot.addCell(labelCell("Total peças"));
      tot.addCell(valueCell(formatMoney(totalPecas), normal, Element.ALIGN_RIGHT));

      PdfPCell totalLabel = labelCell("Total geral");
      totalLabel.setBackgroundColor(new java.awt.Color(240, 240, 240));
      tot.addCell(totalLabel);

      PdfPCell totalValue = valueCell(
          formatMoney(total),
          new Font(Font.HELVETICA, 10, Font.BOLD),
          Element.ALIGN_RIGHT
      );
      totalValue.setBackgroundColor(new java.awt.Color(240, 240, 240));
      tot.addCell(totalValue);

      doc.add(tot);

      // Rodapé simples
      doc.add(Chunk.NEWLINE);
      Paragraph footer = new Paragraph("Documento gerado pelo TorqueOS", small);
      footer.setAlignment(Element.ALIGN_CENTER);
      footer.getFont().setColor(new java.awt.Color(120, 120, 120));
      doc.add(footer);

      doc.close();
      return out.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("Erro ao gerar PDF da OS", e);
    }
  }

  // ===== helpers layout =====

  private static PdfPCell th(String text) {
    PdfPCell c = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 10, Font.BOLD)));
    c.setPadding(6);
    c.setBackgroundColor(new java.awt.Color(245, 245, 245));
    return c;
  }

  private static PdfPCell thRight(String text) {
    PdfPCell c = th(text);
    c.setHorizontalAlignment(Element.ALIGN_RIGHT);
    return c;
  }

  private static PdfPCell labelCell(String text) {
    PdfPCell c = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 10, Font.BOLD)));
    c.setPadding(6);
    c.setBackgroundColor(new java.awt.Color(245, 245, 245));
    return c;
  }

  private static PdfPCell valueCell(String text, Font font) {
    return valueCell(text, font, Element.ALIGN_LEFT);
  }

  private static PdfPCell valueCell(String text, Font font, int align) {
    PdfPCell c = new PdfPCell(new Phrase(text != null ? text : "", font));
    c.setPadding(6);
    c.setHorizontalAlignment(align);
    return c;
  }

  private static String safe(Object o) {
    return o == null ? "" : String.valueOf(o);
  }

  private static String prefixIfNotEmpty(String prefix, String value) {
    String v = (value == null ? "" : value.trim());
    if (v.isEmpty()) return "";
    return prefix + v;
  }

  private static String joinNonEmpty(String sep, String... parts) {
    StringBuilder sb = new StringBuilder();
    if (parts != null) {
      for (String p : parts) {
        if (p != null && !p.trim().isEmpty()) {
          if (sb.length() > 0) sb.append(sep);
          sb.append(p.trim());
        }
      }
    }
    return sb.toString();
  }

  private static String joinEndereco(String endereco, String bairro, String cidade, String uf) {
    String end = (endereco == null ? "" : endereco.trim());
    String bai = (bairro == null ? "" : bairro.trim());
    String cid = (cidade == null ? "" : cidade.trim());
    String u = (uf == null ? "" : uf.trim());

    String cidadeUf = joinNonEmpty(" / ", cid, u);
    String linha = joinNonEmpty(" - ", end, bai, cidadeUf);
    return linha;
  }

  @SuppressWarnings("deprecation")
  private static String formatMoney(BigDecimal v) {
    if (v == null) return "0.00";
    return v.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
  }
}
