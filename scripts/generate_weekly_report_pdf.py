#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Arina Agri - Weekly Update Report PDF Generator
Periode: 19 - 25 Oktober 2026
Target: Android Native Mobile App (Kotlin / Jetpack Compose)
"""

import os
import sys
from reportlab.lib.pagesizes import A4
from reportlab.lib import colors
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, Image, KeepTogether, PageBreak, HRFlowable
)
from reportlab.pdfgen import canvas
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont

# ----------------------------------------------------------------------
# Page Geometry (A4: 595.27 x 841.89 points)
# ----------------------------------------------------------------------
PAGE_WIDTH, PAGE_HEIGHT = A4
MARGIN_LEFT = 40
MARGIN_RIGHT = 40
MARGIN_TOP = 46
MARGIN_BOTTOM = 46
CONTENT_WIDTH = PAGE_WIDTH - MARGIN_LEFT - MARGIN_RIGHT

# ----------------------------------------------------------------------
# Font Registration
# ----------------------------------------------------------------------
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
FONT_DIR = os.path.join(BASE_DIR, "app", "src", "main", "res", "font")

def register_fonts():
    fonts = [
        ("Sora-Bold", os.path.join(FONT_DIR, "sora_bold.ttf")),
        ("Sora-SemiBold", os.path.join(FONT_DIR, "sora_semibold.ttf")),
        ("PlusJakartaSans-Bold", os.path.join(FONT_DIR, "plus_jakarta_sans_bold.ttf")),
        ("PlusJakartaSans-SemiBold", os.path.join(FONT_DIR, "plus_jakarta_sans_semibold.ttf")),
        ("PlusJakartaSans-Medium", os.path.join(FONT_DIR, "plus_jakarta_sans_medium.ttf")),
        ("PlusJakartaSans-Regular", os.path.join(FONT_DIR, "plus_jakarta_sans_regular.ttf")),
    ]
    for font_name, font_path in fonts:
        if os.path.exists(font_path):
            pdfmetrics.registerFont(TTFont(font_name, font_path))
        else:
            print(f"Warning: font not found: {font_path}")

# ----------------------------------------------------------------------
# Numbered Canvas for Running Header & Footer
# ----------------------------------------------------------------------
class NumberedCanvas(canvas.Canvas):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self._saved_page_states = []

    def showPage(self):
        self._saved_page_states.append(dict(self.__dict__))
        self._startPage()

    def save(self):
        num_pages = len(self._saved_page_states)
        for state in self._saved_page_states:
            self.__dict__.update(state)
            self.draw_page_decorations(num_pages)
            super().showPage()
        super().save()

    def draw_page_decorations(self, page_count):
        self.saveState()
        font_regular = "PlusJakartaSans-Regular" if "PlusJakartaSans-Regular" in pdfmetrics.getRegisteredFontNames() else "Helvetica"
        font_bold = "PlusJakartaSans-SemiBold" if "PlusJakartaSans-SemiBold" in pdfmetrics.getRegisteredFontNames() else "Helvetica-Bold"

        # Running Header (pages 2+)
        if self._pageNumber > 1:
            self.setFont(font_bold, 8)
            self.setFillColor(colors.HexColor("#1B4332"))
            self.drawString(MARGIN_LEFT, PAGE_HEIGHT - 30, "Arina Agri")
            self.setFont(font_regular, 8)
            self.setFillColor(colors.HexColor("#52796F"))
            self.drawString(MARGIN_LEFT + 46, PAGE_HEIGHT - 30, "• Laporan Progres & Ringkasan Update Mingguan")
            self.drawRightString(PAGE_WIDTH - MARGIN_RIGHT, PAGE_HEIGHT - 30, "19 – 25 Oktober 2026")
            
            # Header rule
            self.setStrokeColor(colors.HexColor("#D8E2DC"))
            self.setLineWidth(0.6)
            self.line(MARGIN_LEFT, PAGE_HEIGHT - 35, PAGE_WIDTH - MARGIN_RIGHT, PAGE_HEIGHT - 35)

        # Running Footer (all pages)
        self.setFont(font_regular, 8)
        self.setFillColor(colors.HexColor("#60756B"))
        self.drawString(MARGIN_LEFT, 26, "Arina Agri Mobile • 100% Offline-First • Manajemen Keuangan & Perencanaan Usaha")
        page_str = f"Halaman {self._pageNumber} dari {page_count}"
        self.drawRightString(PAGE_WIDTH - MARGIN_RIGHT, 26, page_str)

        # Footer rule
        self.setStrokeColor(colors.HexColor("#D8E2DC"))
        self.setLineWidth(0.6)
        self.line(MARGIN_LEFT, 34, PAGE_WIDTH - MARGIN_RIGHT, 34)

        self.restoreState()

# ----------------------------------------------------------------------
# PDF Builder Function
# ----------------------------------------------------------------------
def generate_weekly_report(output_path):
    register_fonts()

    # Color Palette
    PRIMARY_DARK = colors.HexColor("#1B4332")      # Deep Forest
    PRIMARY_GREEN = colors.HexColor("#029049")     # Official Brand Green
    PRIMARY_MEDIUM = colors.HexColor("#2D6A4F")    # Forest Medium
    ACCENT_MINT = colors.HexColor("#EBF6EE")       # Soft Mint Container
    ACCENT_BORDER = colors.HexColor("#C3E6CB")     # Mint Border
    TEXT_DARK = colors.HexColor("#1D2B24")         # High-contrast readable body text
    TEXT_MUTED = colors.HexColor("#4D6256")        # Secondary text
    BG_LIGHT_CARD = colors.HexColor("#F8FAF9")     # Clean neutral card
    BORDER_LIGHT = colors.HexColor("#DDE5E0")      # Divider border
    STATUS_PASS = colors.HexColor("#029049")       # Pass badge text
    STATUS_PASS_BG = colors.HexColor("#E2F5E9")    # Pass badge bg

    # Font definitions
    title_font = "Sora-Bold" if "Sora-Bold" in pdfmetrics.getRegisteredFontNames() else "Helvetica-Bold"
    heading_font = "Sora-SemiBold" if "Sora-SemiBold" in pdfmetrics.getRegisteredFontNames() else "Helvetica-Bold"
    body_bold = "PlusJakartaSans-Bold" if "PlusJakartaSans-Bold" in pdfmetrics.getRegisteredFontNames() else "Helvetica-Bold"
    body_semi = "PlusJakartaSans-SemiBold" if "PlusJakartaSans-SemiBold" in pdfmetrics.getRegisteredFontNames() else "Helvetica-Bold"
    body_medium = "PlusJakartaSans-Medium" if "PlusJakartaSans-Medium" in pdfmetrics.getRegisteredFontNames() else "Helvetica"
    body_regular = "PlusJakartaSans-Regular" if "PlusJakartaSans-Regular" in pdfmetrics.getRegisteredFontNames() else "Helvetica"

    # Stylesheet
    base_styles = getSampleStyleSheet()
    
    style_doc_title = ParagraphStyle(
        'DocTitle',
        parent=base_styles['Normal'],
        fontName=title_font,
        fontSize=17,
        leading=21,
        textColor=PRIMARY_DARK,
        spaceAfter=3
    )
    
    style_doc_subtitle = ParagraphStyle(
        'DocSubtitle',
        parent=base_styles['Normal'],
        fontName=body_semi,
        fontSize=9.5,
        leading=13,
        textColor=PRIMARY_GREEN,
        spaceAfter=10
    )

    style_meta_label = ParagraphStyle(
        'MetaLabel',
        parent=base_styles['Normal'],
        fontName=body_semi,
        fontSize=8,
        leading=11,
        textColor=PRIMARY_DARK
    )

    style_meta_val = ParagraphStyle(
        'MetaVal',
        parent=base_styles['Normal'],
        fontName=body_regular,
        fontSize=8,
        leading=11,
        textColor=TEXT_DARK
    )

    style_section_h1 = ParagraphStyle(
        'SectionH1',
        parent=base_styles['Normal'],
        fontName=heading_font,
        fontSize=11,
        leading=14,
        textColor=PRIMARY_DARK,
        spaceBefore=10,
        spaceAfter=5,
        keepWithNext=True
    )

    style_body_bullet = ParagraphStyle(
        'BodyBulletCustom',
        parent=base_styles['Normal'],
        fontName=body_regular,
        fontSize=8.5,
        leading=12.5,
        textColor=TEXT_DARK,
        leftIndent=14,
        firstLineIndent=-10,
        spaceAfter=4
    )

    style_callout = ParagraphStyle(
        'CalloutText',
        parent=base_styles['Normal'],
        fontName=body_medium,
        fontSize=8.5,
        leading=12.5,
        textColor=PRIMARY_DARK
    )

    style_th = ParagraphStyle(
        'TH',
        parent=base_styles['Normal'],
        fontName=body_bold,
        fontSize=8,
        leading=10,
        textColor=colors.white,
        alignment=0
    )

    style_td = ParagraphStyle(
        'TD',
        parent=base_styles['Normal'],
        fontName=body_regular,
        fontSize=8,
        leading=11,
        textColor=TEXT_DARK,
        alignment=0
    )

    style_td_status = ParagraphStyle(
        'TDStatus',
        parent=base_styles['Normal'],
        fontName=body_bold,
        fontSize=7.5,
        leading=9,
        textColor=STATUS_PASS,
        alignment=1
    )

    story = []

    # ==================================================================
    # PAGE 1: Header, Metadata, Exec Summary, Section 1, Section 2
    # ==================================================================
    logo_path = os.path.join(BASE_DIR, "docs", "playstore", "app_icon_512x512.png")
    
    header_table_data = []
    if os.path.exists(logo_path):
        app_logo_img = Image(logo_path, width=44, height=44)
        title_flowables = [
            Paragraph("LAPORAN RINGKASAN UPDATE MINGGUAN", style_doc_title),
            Paragraph("Arina Agri Mobile • 100% Offline-First Native Android (Kotlin &amp; Jetpack Compose)", style_doc_subtitle)
        ]
        header_table_data = [[app_logo_img, title_flowables]]
        col_widths = [52, CONTENT_WIDTH - 52]
    else:
        title_flowables = [
            Paragraph("LAPORAN RINGKASAN UPDATE MINGGUAN", style_doc_title),
            Paragraph("Arina Agri Mobile • 100% Offline-First Native Android (Kotlin &amp; Jetpack Compose)", style_doc_subtitle)
        ]
        header_table_data = [[title_flowables]]
        col_widths = [CONTENT_WIDTH]

    header_table = Table(header_table_data, colWidths=col_widths)
    header_table.setStyle(TableStyle([
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('LEFTPADDING', (0,0), (-1,-1), 0),
        ('RIGHTPADDING', (0,0), (-1,-1), 0),
        ('TOPPADDING', (0,0), (-1,-1), 0),
        ('BOTTOMPADDING', (0,0), (-1,-1), 2),
    ]))
    story.append(header_table)

    # Metadata Info Card
    meta_data = [
        [
            Paragraph("<b>Periode Laporan:</b>", style_meta_label),
            Paragraph("19 – 25 Oktober 2026 (Minggu ke-4)", style_meta_val),
            Paragraph("<b>Arsitektur:</b>", style_meta_label),
            Paragraph("Clean Arch, MVVM/MVI, Room SQLite v10", style_meta_val)
        ],
        [
            Paragraph("<b>Target Platform:</b>", style_meta_label),
            Paragraph("Android Native (Kotlin, Jetpack Compose M3)", style_meta_val),
            Paragraph("<b>Status Pengujian:</b>", style_meta_label),
            Paragraph("77 Unit Tests + 54 UI Tests PASS (100%)", style_meta_val)
        ],
        [
            Paragraph("<b>Lisensi &amp; Operasi:</b>", style_meta_label),
            Paragraph("GNU GPLv3 • 100% Offline-First", style_meta_val),
            Paragraph("<b>Catatan ADR:</b>", style_meta_label),
            Paragraph("ADR [DEC-011] s.d. [DEC-035] Disahkan", style_meta_val)
        ]
    ]
    meta_table = Table(meta_data, colWidths=[90, 167, 98, 160])
    meta_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), BG_LIGHT_CARD),
        ('BOX', (0,0), (-1,-1), 0.75, BORDER_LIGHT),
        ('INNERGRID', (0,0), (-1,-1), 0.5, colors.HexColor("#EAEFEA")),
        ('TOPPADDING', (0,0), (-1,-1), 4),
        ('BOTTOMPADDING', (0,0), (-1,-1), 4),
        ('LEFTPADDING', (0,0), (-1,-1), 8),
        ('RIGHTPADDING', (0,0), (-1,-1), 8),
    ]))
    story.append(meta_table)
    story.append(Spacer(1, 8))

    # Executive Summary Box
    exec_summary_html = (
        "<b>Ringkasan Eksekutif:</b> Sepanjang minggu ini (19–25 Oktober 2026), pengembangan aplikasi mobile "
        "Arina Agri berhasil menuntaskan serangkaian milestone transformatif: (1) Rebranding menyeluruh dari Mankeu ke Arina Agri; "
        "(2) Pemurnian buku besar akuntansi ke standar <i>Strict Cash</i> murni tanpa utang/piutang fiktif; "
        "(3) Mesin kalkulasi Rencana Anggaran Biaya (RAB) dan BEP multi-produk musiman; "
        "(4) Perombakan sistem desain visual dengan palet Dark Mode <i>Forest Charcoal</i> dan 6 rumpun aksen warna; "
        "(5) Redesain Pengaturan &amp; Profil terstruktur; "
        "(6) Standarisasi transisi gerak (motion architecture); serta (7) Pusat Bantuan 27 artikel interaktif. "
        "Seluruh tahapan telah terverifikasi lulus 100% pada emulator dan pengujian unit tanpa regresi."
    )
    exec_table = Table([[Paragraph(exec_summary_html, style_callout)]], colWidths=[CONTENT_WIDTH])
    exec_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), ACCENT_MINT),
        ('BOX', (0,0), (-1,-1), 1, ACCENT_BORDER),
        ('LEFTPADDING', (0,0), (-1,-1), 10),
        ('RIGHTPADDING', (0,0), (-1,-1), 10),
        ('TOPPADDING', (0,0), (-1,-1), 7),
        ('BOTTOMPADDING', (0,0), (-1,-1), 7),
    ]))
    story.append(exec_table)
    story.append(Spacer(1, 8))

    # Section 1: Rebranding & Identitas Visual
    story.append(Paragraph("1. Rebranding &amp; Identitas Visual Arina Agri", style_section_h1))
    story.append(Paragraph(
        "• <b>Migrasi Identitas Penuh:</b> Mengganti seluruh nama dan aset warisan ('Mankeu') menjadi 'Arina Agri' pada ikon launcher Android, in-app brand mark, dialog izin otentikasi Google, string lokalisasi (ID/EN), hingga penamaan file ekspor.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Splash Screen Cerdas:</b> Splash screen kini default ke mode terang pada startup awal dan otomatis menyinkronkan tema window OS dengan preferensi tema yang dipilih pengguna (Terang/Gelap/Sistem) melalui UiModeManager.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Aset Play Store Resmi:</b> Memperbarui App Icon 512×512 dan banner Feature Graphic 1024×500 dengan latar belakang putih bersih, tipografi Sora &amp; Plus Jakarta Sans, margin aman 122 px bebas clipping, dan tagline: <i>'100% Offline-First • Pembukuan UMKM dan Petani • RAB &amp; BEP'</i>.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Isolasi Batas Repositori:</b> Menetapkan folder web <code>arina-agri</code> secara ketat sebagai referensi aset &amp; rumus (Read-Only) dan memformalkan lisensi GNU General Public License v3.0 (GPLv3).",
        style_body_bullet
    ))
    story.append(Spacer(1, 6))

    # Section 2: Core Engine & Rencana Usaha
    story.append(Paragraph("2. Core Engine Pembukuan &amp; Rencana Usaha (Schema 8, 9, 10)", style_section_h1))
    story.append(Paragraph(
        "• <b>Prinsip Strict Cash ([DEC-011]):</b> Menghapus 8 tipe transaksi non-kas (kredit, utang, piutang, pinjaman bunga) agar saldo kas mencerminkan uang riil UMKM secara akurat tanpa kalkulasi semu.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Kontrak Template Excel Tunggal ([DEC-012]):</b> Standar workbook Excel resmi yang idempotent—file hasil ekspor dapat diimpor kembali secara lossless (round-trip), dilengkapi rekonsiliasi atomik dan batch rollback.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>RAB &amp; BEP Multi-Produk ([DEC-014], [DEC-016]):</b> Fitur 'Rencana Usaha' menghadirkan kalkulasi resep HPP per siklus, biaya tetap bulanan, penyusutan alat tani/mesin berkala, setoran modal awal, rasio kelayakan usaha (R/C &amp; B/C), serta titik impas (BEP) per produk berdasarkan bauran penjualan.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Produk Musiman &amp; Stok per Unit ([DEC-017]):</b> Field 'Mulai dijual bulan ke-' untuk komoditas hortikultura (misal cabai), sehingga kebutuhan modal kerja mencakup masa tunggu sebelum panen. Manajemen stok kini mendukung kepemilikan mandiri per unit atau bersama lintas unit.",
        style_body_bullet
    ))

    # ==================================================================
    # PAGE 2: Section 3, Section 4, Section 5, Section 6
    # ==================================================================
    story.append(PageBreak())

    # Section 3: Desain UI/UX & Palet Dark Mode
    story.append(Paragraph("3. Desain UI/UX &amp; Palet Dark Mode Forest Charcoal", style_section_h1))
    story.append(Paragraph(
        "• <b>Palet Forest Charcoal ([DEC-034], [DEC-035]):</b> Merombak 48 role warna gelap dengan tema hijau-arang alami bergradasi halus, dilengkapi 6 keluarga aksen seimbang (Mint, Terracotta, Blue, Lavender, Sand, Teal) dengan kontras teks WCAG AA ≥ 4.5:1.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Protokol Warna Semantik Ketat ([DEC-031]):</b> Menegaskan hijau Arina sebagai tombol aksi utama. Warna merah dibatasi secara eksklusif hanya untuk kondisi bahaya/defisit/penghapusan data. Warna amber/kuning untuk status peringatan (warning).",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Penyederhanaan Informasi Kartu (Card Simplification):</b> Komponen <code>CardDetails</code> memangkas kepadatan teks di Beranda dengan menampilkan headline saldo kas dan laba rugi kas, top 3 kategori pengeluaran, serta menyembunyikan rincian teknis di dalam drawer tanpa menghilangkan alert penting.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Empty State Standar &amp; Penuh:</b> Seluruh layar kosong (Catatan, Stok, Laporan) memakai kartu full-height rata tengah dengan ilustrasi Lottie kontekstual dan aksi CTA terarah.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Formulir Transaksi Cepat:</b> Pengelompokan mutasi per tanggal, deskripsi 2-baris aman potong, dan FlowRow chip nominal cepat (+50 rb s.d. +5 jt) dengan target sentuh minimal ≥ 48 dp.",
        style_body_bullet
    ))
    story.append(Spacer(1, 6))

    # Section 4: Pengaturan & Profil Terpadu
    story.append(Paragraph("4. Pengaturan &amp; Profil Terpadu (Hub &amp; Detail)", style_section_h1))
    story.append(Paragraph(
        "• <b>Arsitektur Hub &amp; Detail ([DEC-030]):</b> Mengubah sistem tab Pengaturan menjadi ringkasan utama terkelompok (Overview Hub) dengan navigasi ke halaman detail terfokus: Profil Pemilik &amp; Usaha, Tampilan &amp; Umum, serta Info Sistem.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Paritas Profil dari Web App:</b> Manajemen profil lengkap meliputi Nama Pemilik, Nomor WhatsApp notifikasi (dengan fungsi normalisasi kode negara 62), Akun Telegram, Lokasi Desa, Komoditas Utama, dan Luas Lahan Tani.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Keamanan Draft:</b> Form profil menggunakan <code>rememberSaveable</code> untuk mempertahankan ketikan saat rotasi layar atau navigasi antar tab, dilengkapi tombol simpan eksplisit dan dialog konfirmasi batal.",
        style_body_bullet
    ))
    story.append(Spacer(1, 6))

    # Section 5: Arsitektur Motion & Aksesibilitas
    story.append(Paragraph("5. Arsitektur Gerakan (Motion) &amp; Aksesibilitas", style_section_h1))
    story.append(Paragraph(
        "• <b>Standarisasi Transisi Layar ([DEC-032]):</b> Sequential fade-through (exit 90ms, enter 180ms) antar menu utama untuk menghilangkan efek layar goyang/melayang; Shared-axis X horizontal (24dp) untuk pergantian mode Realisasi ↔ Proyeksi; dan Shared-axis Z untuk bottom sheet dialog.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Kepatuhan Aksesibilitas Sistem:</b> Mendukung 3 tingkat gerak: <i>Full</i> (animasi ekspresif), <i>Reduced</i> (hanya fade ≤ 140 ms tanpa pergeseran posisi), dan <i>Off</i> (transisi instan 0ms) secara otomatis menghormati setelan animasi sistem Android.",
        style_body_bullet
    ))
    story.append(Spacer(1, 6))

    # Section 6: Pusat Bantuan & Aset Lottie Offline
    story.append(Paragraph("6. Pusat Bantuan &amp; Aset Lottie Offline", style_section_h1))
    story.append(Paragraph(
        "• <b>Pusat Bantuan Komprehensif ([DEC-018]):</b> 27 artikel panduan tugas (ID &amp; EN) dengan fitur pencarian cepat, tangkapan layar langkah-demi-langkah, dan tombol interaktif <i>'Tunjukkan di aplikasi'</i> yang membuka layar terkait dan menyorot elemen fitur.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Tur Panduan Terarah:</b> Spotlight tour interaktif yang santun (ditawarkan melalui kartu saran kecil, tidak memaksa) dan dapat dinonaktifkan permanen via Pengaturan.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Animasi Lottie Offline ([DEC-026]):</b> 5 animasi kontekstual (Rencana Usaha, Laporan Kosong, Stok Barang, Impor Excel, Backup Cloud) tersimpan lokal dan beradaptasi dinamis pada palet terang/gelap tanpa perlu koneksi internet.",
        style_body_bullet
    ))

    # ==================================================================
    # PAGE 3: Section 7, Section 8, Sign-off Box
    # ==================================================================
    story.append(PageBreak())

    # Section 7: Performa, Stabilitas, & Perbaikan Bug
    story.append(Paragraph("7. Performa, Stabilitas, &amp; Perbaikan Bug Kritis", style_section_h1))
    story.append(Paragraph(
        "• <b>Offloading Kalkulasi Berat ([DEC-025]):</b> Seluruh perhitungan BEP, depresiasi, dan arus kas bulanan dipindahkan ke background dispatcher (<code>Dispatchers.Default</code>), membebaskan Main UI thread dari lag dan frame-drop.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Eliminasi Loop Rekomposisi Vico:</b> Memperbaiki bug render tak terbatas pada grafik arus kas bulanan dengan membungkus dataset dalam <code>remember</code>.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Perbaikan Overlap Impor:</b> Mengatasi tumpang tindih visual antar elemen kontrol pratinjau impor Excel di <code>MoreScreen.kt</code> dengan wadah kolom tunggal terisolasi.",
        style_body_bullet
    ))
    story.append(Paragraph(
        "• <b>Optimasi R8 &amp; APK Release:</b> Mengaktifkan R8 code shrinking dan inlining Jetpack Compose untuk meminimalkan ukuran file biner APK dan memaksimalkan kecepatan cold start.",
        style_body_bullet
    ))
    story.append(Spacer(1, 8))

    # Section 8: Matriks Quality Gates & Hasil Pengujian Kualitas
    story.append(Paragraph("8. Matriks Quality Gates &amp; Hasil Pengujian Kualitas", style_section_h1))
    story.append(Paragraph("Seluruh perubahan diverifikasi menggunakan pipeline pengujian otomatis bertingkat:", ParagraphStyle(
        'IntroTable', parent=base_styles['Normal'], fontName=body_regular, fontSize=8.5, leading=11.5, textColor=TEXT_DARK, spaceAfter=5
    )))

    qa_headers = [
        Paragraph("<b>Kategori Pengujian</b>", style_th),
        Paragraph("<b>Cakupan / Target Komponen</b>", style_th),
        Paragraph("<b>Hasil Verifikasi Nyata</b>", style_th),
        Paragraph("<b>Status</b>", style_th)
    ]

    qa_rows = [
        qa_headers,
        [
            Paragraph("Unit Testing (JUnit)", style_td),
            Paragraph("Kalkulator BEP, rumus HPP, model kas murni, normalisasi kontak, migrasi Room", style_td),
            Paragraph("77 unit tests lulus 100% (0 error, 0 skip)", style_td),
            Paragraph("LULUS", style_td_status)
        ],
        [
            Paragraph("UI &amp; Connected Test", style_td),
            Paragraph("Emulator Android: CardDetails, import layout, alur workflow, visual system", style_td),
            Paragraph("54 instrumented cases lulus tanpa gagal", style_td),
            Paragraph("LULUS", style_td_status)
        ],
        [
            Paragraph("Android Lint", style_td),
            Paragraph("Pemeriksaan sintaksis, dependensi usang, resource leak, validitas XML", style_td),
            Paragraph("BUILD SUCCESSFUL (0 Lint Errors)", style_td),
            Paragraph("LULUS", style_td_status)
        ],
        [
            Paragraph("Aksesibilitas &amp; Kontras", style_td),
            Paragraph("Audit rasio kontras warna terang/gelap, ukuran sentuh minimal 48dp, TalkBack", style_td),
            Paragraph("334 checks / 204 pairs WCAG AA ≥ 4.5:1", style_td),
            Paragraph("LULUS", style_td_status)
        ],
        [
            Paragraph("Lokalisasi Bahasa", style_td),
            Paragraph("Sinkronisasi resource string Bahasa Indonesia dan English (values &amp; values-en)", style_td),
            Paragraph("741 string keys sinkron 1:1 tanpa selisih", style_td),
            Paragraph("LULUS", style_td_status)
        ],
        [
            Paragraph("Tata Kelola Log &amp; ADR", style_td),
            Paragraph("Modular Logging Protocol (ACTIVITY_LOG, DECISION_LOG, CHANGELOG)", style_td),
            Paragraph("ADR DEC-011 s.d. DEC-035 disahkan", style_td),
            Paragraph("LULUS", style_td_status)
        ],
    ]

    qa_table = Table(qa_rows, colWidths=[110, 185, 160, 60], repeatRows=1)
    qa_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), PRIMARY_DARK),
        ('ALIGN', (0,0), (-1,-1), 'LEFT'),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('GRID', (0,0), (-1,-1), 0.5, BORDER_LIGHT),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.white, BG_LIGHT_CARD]),
        ('TOPPADDING', (0,0), (-1,-1), 4.5),
        ('BOTTOMPADDING', (0,0), (-1,-1), 4.5),
        ('LEFTPADDING', (0,0), (-1,-1), 6),
        ('RIGHTPADDING', (0,0), (-1,-1), 6),
        ('BACKGROUND', (3,1), (3,-1), STATUS_PASS_BG),
    ]))
    story.append(qa_table)
    story.append(Spacer(1, 14))

    # Sign-off Box
    sign_off_text = (
        "<b>Catatan Penutup:</b> Aplikasi Arina Agri berada dalam kondisi stabil, bersih dari regresi, "
        "memiliki performa responsif, dan siap untuk tahap pengujian pengguna (UAT) serta pemaketan rilis berikutnya.<br/>"
        "<i>Diterbitkan secara resmi untuk tim pengembang dan stakeholder Arina Agri pada 25 Oktober 2026.</i>"
    )
    sign_table = Table([[Paragraph(sign_off_text, style_callout)]], colWidths=[CONTENT_WIDTH])
    sign_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), BG_LIGHT_CARD),
        ('BOX', (0,0), (-1,-1), 0.75, BORDER_LIGHT),
        ('LEFTPADDING', (0,0), (-1,-1), 10),
        ('RIGHTPADDING', (0,0), (-1,-1), 10),
        ('TOPPADDING', (0,0), (-1,-1), 8),
        ('BOTTOMPADDING', (0,0), (-1,-1), 8),
    ]))
    story.append(sign_table)

    # ------------------------------------------------------------------
    # Build Document
    # ------------------------------------------------------------------
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    doc = SimpleDocTemplate(
        output_path,
        pagesize=A4,
        leftMargin=MARGIN_LEFT,
        rightMargin=MARGIN_RIGHT,
        topMargin=MARGIN_TOP,
        bottomMargin=MARGIN_BOTTOM
    )
    doc.build(story, canvasmaker=NumberedCanvas)
    print(f"PDF generated successfully at: {output_path}")

if __name__ == "__main__":
    out_dir = os.path.join(BASE_DIR, "docs", "reports")
    out_file = os.path.join(out_dir, "LAPORAN_UPDATE_MINGGUAN_2026-10-25.pdf")
    generate_weekly_report(out_file)
