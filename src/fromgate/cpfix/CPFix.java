/*  
 *  CPFix, Minecraft bukkit plugin
 *  (c)2013, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/cpfix/
 *    
 *  This file is part of CPFix.
 *  
 *  CPFix is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CPFix is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CPFix.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package fromgate.cpfix;

import java.io.BufferedReader;
import sun.misc.SharedSecrets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;


public class CPFix extends JavaPlugin {
	CPFUtil u;
	CPFListener l;

	// Конфигурация
	boolean vcheck = true;
	String language = "russian";
	boolean language_save=false;
	String system_codepage = "UTF8";
	String cp_from = "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ¸¨";
	String cp_to   = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюяёЁ";
	boolean fix_chat = true;
	boolean fix_cmd = true;
	boolean fix_sign = true;
	boolean fix_books = true;
	boolean fix_names = true;
	boolean inform_player = true;

	// Output recoding
	boolean recode_console=true;
	String cp_console = "CP866";
	boolean recode_logfile=true;
	String cp_logfile = "CP1251";

	// Input recoding
	boolean recode_input = false;
	String cp_console_input = "CP866";

	// Whitelist characters
	boolean wl_char = true;
	String whitelist =" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_¸abcdefghijklmnopqrstuvwxyz{|}~АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЫЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
	String wl_replace = "_";

	@Override
	public void onEnable() {
		loadCfg();
		saveCfg();
		u = new CPFUtil(this, vcheck, language_save, language, "cpfix", "CPFix", "cpfix", "&b[&3CPFix&b]&f ");
		l = new CPFListener (this);
		getCommand("cpfix").setExecutor(u);
		getServer().getPluginManager().registerEvents(l, this);

		setConsoleAndLogCodePage();

		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (IOException e) {
		}		
	}

	public void saveCfg() {
		getConfig().set("general.check-updates", vcheck);
		getConfig().set("general.language",language);
		getConfig().set("general.language-save",language_save);
		getConfig().set("white-list.enable",wl_char);
		//getConfig().set("white-list.characters",whitelist);
		getConfig().set("white-list.replace",wl_replace);
		getConfig().set("code-page.chat-fix-enable", fix_chat);
		getConfig().set("code-page.command-fix-enable", fix_cmd);
		getConfig().set("code-page.sign-fix-enable", fix_sign);
		getConfig().set("code-page.book-fix-enable", fix_books);
		getConfig().set("code-page.lore-fix-enable", fix_names);
		getConfig().set("code-page.inform-player", inform_player);
		getConfig().set("output-recode.console.enable",recode_console);
		getConfig().set("output-recode.console.code-page",cp_console);
		getConfig().set("output-recode.server-log.enable",recode_logfile);
		getConfig().set("output-recode.server-log.code-page",cp_logfile);
		getConfig().set("input-recode.enable",recode_input);
		getConfig().set("input-recode.code-page",cp_console_input);
		saveConfig();
		saveCharFile();
	}

	public void loadCharFile(){
		File f = new File (this.getDataFolder()+File.separator+"characters.txt");
		if (f.exists()){
			try {
				BufferedReader bfr = new BufferedReader(new InputStreamReader (new FileInputStream (f),"UTF8"));
				String ln = bfr.readLine(); 
				if (!ln.isEmpty()) cp_from = ln;
				ln = bfr.readLine(); //second line
				if (!ln.isEmpty()) cp_to = ln;
				ln = bfr.readLine(); // third line
				if (!ln.isEmpty()) whitelist  = ln;
				bfr.close();
			} catch (Exception e) {
			}
			return;
		}
	}

	public void saveCharFile(){
		File f = new File (this.getDataFolder()+File.separator+"characters.txt");
		if (f.exists()) f.delete();
		try {
			f.createNewFile();
			BufferedWriter bwr = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (f), "UTF8"));
			bwr.write(cp_from+"\n");
			bwr.write(cp_to+"\n");
			bwr.write(whitelist+"\n");
			bwr.flush();
			bwr.close();
		} catch (Exception e) {
		}
	}

	public void loadCfg() {
		vcheck = getConfig().getBoolean("general.check-updates", true);
		language = getConfig().getString("general.language","russian");
		language_save=getConfig().getBoolean("general.language-save",false);
		fix_chat = getConfig().getBoolean("code-page.chat-fix-enable", true);
		fix_cmd = getConfig().getBoolean("code-page.command-fix-enable", true);
		fix_sign = getConfig().getBoolean("code-page.sign-fix-enable", true);
		fix_books = getConfig().getBoolean("code-page.book-fix-enable", true);
		fix_names = getConfig().getBoolean("code-page.lore-fix-enable", false);
		inform_player = getConfig().getBoolean("code-page.inform-player", true);
		recode_console=getConfig().getBoolean("output-recode.console.enable",false);
		cp_console = getConfig().getString("output-recode.console.code-page",getSystemConsoleCodepage());
		recode_logfile=getConfig().getBoolean("output-recode.server-log.enable",false);
		cp_logfile=getConfig().getString("output-recode.server-log.code-page",getSystemConsoleCodepage());
		recode_input=getConfig().getBoolean("input-recode.enable",false);
		cp_console_input=getConfig().getString("input-recode.code-page",getSystemConsoleCodepage());
		wl_char = getConfig().getBoolean("white-list.enable",true);
		wl_replace =getConfig().getString("white-list.replace","_");
		loadCharFile();
	}

	public String recodeText (String str){
		String nstr = str;
		if (!str.isEmpty()){
			for (int i = 0; i<cp_from.length();i++)
				nstr = nstr.replace(cp_from.charAt(i), cp_to.charAt(i));
		}
		return nstr;
	}

	public String refilterText (String str){
		if (str.isEmpty()) return str;
		String nstr = str;
		for (int i = 0; i<str.length();i++)
			if (!whitelist.contains(String.valueOf(str.charAt(i))))
				nstr = nstr.replace(String.valueOf(str.charAt(i)), wl_replace);
		return nstr;
	}

	public void inform(CommandSender sender){
		if (sender instanceof Player){
			Player p = (Player) sender;
			if (p.hasMetadata("CPFix-informed")) return;
			u.printMSG(p, "msg_wrongcp",'c');
			p.setMetadata("CPFix-informed", new FixedMetadataValue (this, true));
		}
	}

	public void fixItemNameAndLore (ItemStack item){
		ItemMeta im = item.getItemMeta();
		if (im.hasDisplayName())
			im.setDisplayName(recodeText(im.getDisplayName()));
		if (im.hasLore()){
			List<String> il = im.getLore();
			if (il.size()>0)
				for (int i = 0; i<il.size();i++)
					il.set(i, recodeText(il.get(i)));
		}
		item.setItemMeta(im);
	}

	public BookMeta fixBook (BookMeta bm){
		if (bm.hasAuthor()) bm.setAuthor(recodeText(bm.getAuthor()));
		if (bm.hasTitle()) bm.setTitle(recodeText (bm.getTitle()));
		if (bm.hasPages()){
			List<String> pages = recodeList(bm.getPages());
			bm.setPages(pages);
		}
		return bm;
	}

	public void fixSign (Sign sign){
		for (int i = 0; i<4; i++)
			if (!sign.getLine(i).isEmpty())
				sign.setLine(i, recodeText(sign.getLine(i)));
		sign.update();
	}

	public List<String> recodeList(List<String> lines){
		List<String> ln = new ArrayList<String>();
		if (!lines.isEmpty())
			for (int i = 0; i<lines.size();i++)
				ln.add(recodeText(lines.get(i)));
		return ln;
	}

	// Определение кодировки системной консоли
	public String getSystemConsoleCodepage(){
		if (System.console() != null) system_codepage = SharedSecrets.getJavaIOAccess().charset().name();
		return system_codepage;
	}

	// Определение кодировки консоли сервера (по идее работает, только если выставлена принудительно)
	public String getServerConsoleCodepage(){
		Logger log = Logger.getLogger("Minecraft");
		Handler[] hs = log.getParent().getHandlers();
		try {
			for (Handler h : hs)
				if ((h instanceof ConsoleHandler)&&(h.getEncoding() != null)) return h.getEncoding();
		} catch (Exception e) {
		}
		return u.getMSGnc("unknown");
	}

	// Определение кодировки журнального файла (по идее работает, только если выставлена принудительно)
	public String getLogCodepage(){
		Logger log = Logger.getLogger("Minecraft");
		Handler[] hs = log.getParent().getHandlers();
		try {
			for (Handler h : hs)
				if ((h instanceof FileHandler)&&(h.getEncoding() != null)) return h.getEncoding();
		} catch (Exception e) {
		}
		return u.getMSGnc("unknown");
	}

	public boolean setConsoleAndLogCodePage(){
		Logger log = Logger.getLogger("Minecraft");
		Handler[] hs = log.getParent().getHandlers();
		if (!(recode_logfile||recode_console)) return false;
		if (hs.length==0) return false;
		try {
			for (Handler h : hs){
				if (recode_logfile&&(h instanceof FileHandler)) h.setEncoding(cp_logfile);
				else if (recode_console&&(h instanceof ConsoleHandler)) h.setEncoding(cp_console);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public String recodeToUTF8(String str, String cp){
		try {
			return new String (str.getBytes(),cp);
		} catch (Exception e) {
		}
		return str;
	}

	public void autoConfig(){
		String cp = getSystemConsoleCodepage();
		recode_console = true;
		cp_console = cp;
		recode_input = true;
		cp_console_input = cp;
		if (!recode_logfile){ 
			cp_logfile = cp;
			recode_logfile=true;
		}
		saveCfg();
	}
}
